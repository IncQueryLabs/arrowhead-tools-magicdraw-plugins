package com.incquerylabs.floodtest.dds;

import java.time.Instant;

import com.incquerylabs.floodtest.Constants;
import com.incquerylabs.floodtest.Consumer;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.StringSeq;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;

public class DdsConsumer extends DataReaderAdapter implements Consumer{
	
	DomainParticipant participant;
	Publisher publisher;
	StringDataWriter writer;
	Subscriber subscriber;
	StringDataReader reader;
	Topic forwardTopic;
	Topic backwardTopic;
	Instant start;
	Instant end;
	private volatile boolean waitForResponse = true;
	StringSeq dataSeq = new StringSeq();
	SampleInfoSeq infoSeq = new SampleInfoSeq();

	public DdsConsumer() {
		participant = DomainParticipantFactory.TheParticipantFactory.create_participant(Constants.DDS_DOMAIN_NUMBER,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		publisher = participant.create_publisher(DomainParticipant.PUBLISHER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		String typeName = StringTypeSupport.get_type_name();
		forwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_FORWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		backwardTopic = participant.create_topic(Constants.DDS_PROCESSOR_BACKWARD_TOPIC_NAME, typeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		writer = (StringDataWriter) publisher.create_datawriter(forwardTopic, Publisher.DATAWRITER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		reader = (StringDataReader) subscriber.create_datareader(backwardTopic, Subscriber.DATAREADER_QOS_DEFAULT,
				this, StatusKind.STATUS_MASK_ALL);
	}

	@Override
	public void go() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Instant getEnd() {
		return end;
	}

	@Override
	public Instant getStart() {
		return start;
	}
}
