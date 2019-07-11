package com.incquerylabs.onetoonelatencytest.dds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.util.Arrays;

import com.incquerylabs.onetoonelatencytest.Constants;
import com.incquerylabs.onetoonelatencytest.Sender;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.ByteSeq;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

public class DdsSend extends DataReaderAdapter implements Sender{
	
	DomainParticipant participant;
	Publisher publisher;
	DdsFileDataWriter writer;
	Subscriber subscriber;
	DdsFileDataReader reader;
	Topic forwardTopic;
	Topic backwardTopic;
	Map<Integer, Instant> times = new HashMap<Integer, Instant>();
	private static final int BUFFER_SIZE = 64000;
	private volatile boolean waitForResponse = true;
	DdsFileSeq dataSeq = new DdsFileSeq();
	SampleInfoSeq infoSeq = new SampleInfoSeq();

	public DdsSend() {
		participant = DomainParticipantFactory.TheParticipantFactory.create_participant(Constants.DDS_DOMAIN_ID,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		publisher = participant.create_publisher(DomainParticipant.PUBLISHER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		String typeName = DdsFileTypeSupport.get_type_name();
		DdsFileTypeSupport.register_type(participant, typeName);
		forwardTopic = participant.create_topic(Constants.DDS_FORWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		backwardTopic = participant.create_topic(Constants.DDS_BACKWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		writer = (DdsFileDataWriter) publisher.create_datawriter(forwardTopic, Publisher.DATAWRITER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		reader = (DdsFileDataReader) subscriber.create_datareader(backwardTopic, Subscriber.DATAREADER_QOS_DEFAULT,
				this, StatusKind.STATUS_MASK_ALL);
	}

	@Override
	public void send(int n, File file) {
		times.put(0, Instant.now());
		byte[] buff = new byte[BUFFER_SIZE];
		FileInputStream fip = null;
		for(int i = 0; i < n; ++i) {
			try {
				fip = new FileInputStream(file);
				ByteBuffer buf = ByteBuffer.wrap(buff);
				buf.putLong(file.length());
				fip.read(buff, 8, BUFFER_SIZE - 8);
				DdsFile instance = new DdsFile();
				instance.chunk = new ByteSeq(buff);
				writer.write(instance, null);
				System.out.println("DDS message number " + Integer.valueOf(i + 1) + " sent.");
				int count = 0;
				while((count = fip.read(buff)) > 0) {
					byte[] bytesRead = Arrays.copyOfRange(buff, 0, count);
					instance = new DdsFile();
					instance.chunk = new ByteSeq(bytesRead);
					writer.write(instance, null);
				}
			} catch (FileNotFoundException e) {
				System.out.println("Problems on file opening");
			} catch (IOException e) {
				System.out.println("Problems reading opening");
			} finally {
				if(fip != null) {
					try {
						fip.close();
					} catch (IOException e) {
						System.out.println("Problems on file closing");
					}
				}
			}
			while (waitForResponse) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("DDS Send interrupted while waiting for confirmation.");
				}
			}

			waitForResponse = true;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("DDS Send interrupted while cleaning up.");
			}
			times.put(Integer.valueOf(i + 1), Instant.now());
		}
	}

	@Override
	public void kill() {
		if (participant != null) {
			participant.delete_contained_entities();
			DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
			participant = null;
		}
	}

	@Override
	public Map<Integer, Instant> getTimes() {
		return times;
	}
	
	@Override
	public void on_data_available(DataReader arg0) {
		DdsFileDataReader getter = (DdsFileDataReader) arg0;

		try {
			getter.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
					SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
					InstanceStateKind.ANY_INSTANCE_STATE);
			for(int i= 0; i < dataSeq.size(); ++i) {
				if(infoSeq.get(i).valid_data) {
					waitForResponse = false;
				}
			}
		} catch (Exception e) {
			System.out.println("Problems in ddsrec listener");
		} finally {
			getter.return_loan(dataSeq, infoSeq);
		}
	}
}
