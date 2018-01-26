package com.eitccorp.demo.sofwerx.parser;

import bdp.ingest.api.model.FileInfo;
import bdp.ingest.api.model.Record;
import bdp.ingest.parsers.util.IngestInfoParser;
import org.calrissian.mango.collect.AbstractCloseableIterator;
import org.calrissian.mango.collect.CloseableIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This is an Email Parser
 */
public class EmailParser extends IngestInfoParser<Record> {

    public static final String FROM = "From:";
    public static final String SENT = "Sent:";
    public static final String SUBJECT = "Subject:";
    public static final String DATE = "Date:";
    public static final String TO = "To:";

    @Override
    public CloseableIterator<Record> parseRaw(InputStream inputStream, FileInfo fileInfo) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        return new AbstractCloseableIterator<Record>() {
            @Override
            protected Record computeNext() {
                try {
                    String line;
                    Record record = null;
                    StringBuilder body = null;
                    Boolean isFirstRecord = true;
                    while((line = reader.readLine()) != null){
                        if(line.startsWith(FROM)) {
                            if(!isFirstRecord){
                                record.getData().put("//message//smtp", body.toString().trim());
                                record.getData().put("//orderedlist//fileids//attachments//smtp", "mp4/state_of_the_nation");
                                reader.reset();
                                return record;
                            }

                            isFirstRecord = false;

                            record = new Record();
                            record.getData().put("//from//smtpheader", line.replaceFirst(FROM, "").trim());
                            body = new StringBuilder();
                        }
                        else if(line.startsWith(TO)){
                            record.getData().put("//to//smtpheader", line.replaceFirst(TO, "").trim());
                        }
                        else if(line.startsWith(SENT)){
                            record.getData().put("//datetime//smtpheader", line.replaceFirst(SENT, "").trim());
                        }
                        else if(line.startsWith(DATE)){
                            record.getData().put("//datetime", line.replaceFirst(DATE, "").trim());
                        }
                        else if(line.startsWith(SUBJECT)){
                            record.getData().put("//subject//smtpheader", line.replaceFirst(SUBJECT, "").trim());
                        }
                        else{
                            body.append(line);
                            body.append("\n");
                            reader.mark(300);
                        }
                    }

                    return endOfData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }
}
