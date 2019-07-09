package com.incquerylabs.onetoonelatencytest.dds;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.onetoonelatencytest.Receiver;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
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

	public static int domainId = DdsSend.domainId;
	int samplecount = 0;
	DomainParticipant participant = null;
	Subscriber subscriber = null;
	public static final String TOPIC_NAME = DdsSend.TOPIC_NAME;
	Topic topic = null;
	DataReaderListener listener = null;
	FileChunkTypeDataReader reader = null;
	Map<Integer, Instant> mid = new HashMap<Integer, Instant>();
	Map<Integer, Instant> end = new HashMap<Integer, Instant>();

	public DdsRec() {
		participant = DomainParticipantFactory.TheParticipantFactory.create_participant(domainId,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (participant == null) {
			System.err.println("create_participant error\n");
			return;
		}

		subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		if (subscriber == null) {
			System.err.println("create_subscriber error\n");
			return;
		}

		String typeName = FileChunkTypeTypeSupport.get_type_name();
		FileChunkTypeTypeSupport.register_type(participant, typeName);

		topic = participant.create_topic(TOPIC_NAME, typeName, DomainParticipant.TOPIC_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		if (topic == null) {
			System.err.println("create_topic error\n");
			return;
		}

		listener = new FileChunkTypeListener();

		reader = (FileChunkTypeDataReader) subscriber.create_datareader(topic, Subscriber.DATAREADER_QOS_DEFAULT,
				listener, StatusKind.STATUS_MASK_ALL);
		if (reader == null) {
			System.err.println("create_datareader error\n");
			return;
		}
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

	private class FileChunkTypeListener extends DataReaderAdapter {

		FileChunkTypeSeq dataSeq = new FileChunkTypeSeq();
		SampleInfoSeq infoSeq = new SampleInfoSeq();

		public void on_data_available(DataReader reader) {
			FileChunkTypeDataReader fileChunkTypeReader = (FileChunkTypeDataReader) reader;

			try {
				fileChunkTypeReader.take(dataSeq, infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
						SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
						InstanceStateKind.ANY_INSTANCE_STATE);
				
				SampleInfo info = (SampleInfo) infoSeq.get(0);
				FileChunkType fct = null;
				Integer index = null;
				if(info.valid_data) {
					fct = dataSeq.get(0);
					index = Integer.parseInt(fct.filename);
					if(mid.get(index) == null) {
						mid.put(index, Instant.now());
						System.out.println("Rec: " + index + " at: " + Instant.now().toString());
					}
				}
				for (int i = 1; i < dataSeq.size(); ++i) {
					info = (SampleInfo) infoSeq.get(i);
					dataSeq.get(i);
				}
				end.put(index, Instant.now());
			} catch (RETCODE_NO_DATA noData) {
				// No data to process
			} finally {
				fileChunkTypeReader.return_loan(dataSeq, infoSeq);
			}
		}
	}
}
