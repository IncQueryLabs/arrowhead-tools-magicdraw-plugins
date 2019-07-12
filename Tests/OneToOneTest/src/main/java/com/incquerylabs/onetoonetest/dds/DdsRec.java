package com.incquerylabs.onetoonetest.dds;

import java.nio.ByteBuffer;

import com.incquerylabs.onetoonetest.Constants;
import com.incquerylabs.onetoonetest.Receiver;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.ByteSeq;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.DataReaderListener;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

public class DdsRec extends Thread implements Receiver {

	DomainParticipant participant;
	Publisher publisher;
	DdsFileDataWriter writer;
	Subscriber subscriber;
	DataReaderListener listener = new DdsFileListener();
	DdsFileDataReader reader;
	Topic forwardTopic;
	Topic backwardTopic;

	public DdsRec() {
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
		writer = (DdsFileDataWriter) publisher.create_datawriter(backwardTopic, Publisher.DATAWRITER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		reader = (DdsFileDataReader) subscriber.create_datareader(forwardTopic, Subscriber.DATAREADER_QOS_DEFAULT,
				listener, StatusKind.STATUS_MASK_ALL);
	}

	@Override
	public void kill() {
		if (participant != null) {
			participant.delete_contained_entities();
			DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
			participant = null;
		}
		this.interrupt();
	}

	private class DdsFileListener extends DataReaderAdapter {
		DdsFileSeq dataSeq = new DdsFileSeq();
		SampleInfoSeq infoSeq = new SampleInfoSeq();
		long fileSize = 0;
		long readSize = 0;

		@Override
		public void on_data_available(DataReader arg0) {
			DdsFileDataReader getter = (DdsFileDataReader) arg0;

			try {
				getter.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
						SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
						InstanceStateKind.ANY_INSTANCE_STATE);
				for(int i= 0; i < dataSeq.size(); ++i) {
					SampleInfo info = infoSeq.get(i);
					if(info.valid_data) {
						ByteSeq chunk = dataSeq.get(i).chunk;
						if(fileSize == 0) {
							System.out.println("DDS message received.");
							byte[] bytes = new byte[8];
							for(int q = 0; q < 8; ++q) {
								bytes[q] = chunk.getByte(q);
							}
							ByteBuffer buf = ByteBuffer.wrap(bytes);
							fileSize = buf.getLong();
						}
						readSize = readSize + chunk.size();
						if(readSize >= fileSize) {
							byte[] bytes = {'g', 'g'};
							DdsFile instance = new DdsFile();
							instance.chunk = new ByteSeq(bytes);
							writer.write(instance, null);
							fileSize = 0;
							readSize = 0;
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Problems in ddsrec listener");
			} finally {
				getter.return_loan(dataSeq, infoSeq);
			}
		}
	}
}
