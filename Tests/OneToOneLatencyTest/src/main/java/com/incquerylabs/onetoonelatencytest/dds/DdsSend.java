package com.incquerylabs.onetoonelatencytest.dds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.incquerylabs.onetoonelatencytest.Sender;
import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.ByteSeq;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;

public class DdsSend implements Sender {

	public static int domainId = 0;
	int samplecount = 0;
	DomainParticipant participant = null;
	Publisher publisher = null;
	Topic topic = null;
	FileChunkTypeDataWriter writer = null;
	public static final String TOPIC_NAME = "Chunkyfile";
	Map<Integer, Instant> times = new HashMap<Integer, Instant>();

	public DdsSend() {
		participant = DomainParticipantFactory.TheParticipantFactory.create_participant(domainId,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (participant == null) {
			System.err.println("create_participant error\n");
			return;
		}

		publisher = participant.create_publisher(DomainParticipant.PUBLISHER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		if (publisher == null) {
			System.err.println("create_publisher error\n");
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

		writer = (FileChunkTypeDataWriter) publisher.create_datawriter(topic, Publisher.DATAWRITER_QOS_DEFAULT, null,
				StatusKind.STATUS_MASK_NONE);
		if (writer == null) {
			System.err.println("create_datawriter error\n");
			return;
		}
	}

	@Override
	public void send(int n, File file) {
		FileChunkType chunk = new FileChunkType();
		InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;
		try {
			ByteSeq bytes = new ByteSeq();
			byte[] buff = Files.readAllBytes(file.toPath());
			byte[] buf;
			for(int i = 0; i < buff.length; i += 63308) {
				buf = Arrays.copyOfRange(buff, i, i+63308);
				bytes.addAllByte(buf);
				chunk.filename = Integer.valueOf(n).toString();
				chunk.data = bytes;
				writer.write(chunk, instance_handle);
				bytes.clear();
			}
		} catch (FileNotFoundException e) {
			System.out.println("DDS send error on file open");
		} catch (IOException e) {
			System.out.println("DDS send error on file read");
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
}
