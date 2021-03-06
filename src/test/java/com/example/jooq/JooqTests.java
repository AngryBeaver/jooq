package com.example.jooq;

import com.example.jooq.config.PersistenceContext;
import com.example.jooq.tables.records.JooqtestRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.Test;

import static com.example.jooq.tables.Jooqtest.JOOQTEST;

import com.example.jooq.tables.Jooqtest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.Random;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceContext.class})
public class JooqTests {

    @Autowired
    DSLContext create;

    private Random r = new Random();

    @Test
    public void testInsertSingles() {
        // All of these tables were generated by jOOQ's Maven plugin
        JooqtestRecord record1 = new JooqtestRecord();
        record1.setInt(10L);
        JooqtestRecord record2 = new JooqtestRecord();
        record2.setChar("Test");
        JooqtestRecord record3 = new JooqtestRecord();
        record3.setTime(new Timestamp(1));
        create.insertInto(JOOQTEST)
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record2).newRecord()
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record1).newRecord()
                .set(record3).execute();
    }

    @Test
    public void testInsertDuals() {
        JooqtestRecord record1 = new JooqtestRecord();
        record1.setInt(10L);
        record1.setChar("Test");
        JooqtestRecord record2 = new JooqtestRecord();
        record2.setInt(10L);
        record2.setTime(new Timestamp(1));
        JooqtestRecord record3 = new JooqtestRecord();
        record3.setChar("Test");
        record3.setTime(new Timestamp(1));
        JooqtestRecord record4 = new JooqtestRecord();
        record4.setChar("Test");
        JooqtestRecord record5 = new JooqtestRecord();
        record5.setTime(new Timestamp(1));
        JooqtestRecord record6 = new JooqtestRecord();
        record6.setInt(10L);

        create.insertInto(JOOQTEST)
                .set(record1).newRecord()
                .set(record2).newRecord()
                .set(record3).newRecord()
                .set(record4).newRecord()
                .set(record5).newRecord()
                .set(record6).newRecord()
                .set(record1).newRecord()
                .set(record2).newRecord()
                .set(record3).newRecord()
                .set(record4).newRecord()
                .set(record5).newRecord()
                .set(record6).execute();
    }

    @Test
    public void testInsertMayBes () {
        create.insertInto(JOOQTEST)
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).newRecord()
                .set(getRandomRecord()).execute();
    }

    @Test
    public void testInsertBugRecordSeq() {
        create.insertInto(JOOQTEST)
                .set(getBugRecord(false)).newRecord()
                .set(getBugRecord(true)).newRecord()
                .set(getBugRecord(false)).newRecord()
                .set(getRandomRecord()).execute();
    }

    @Test
    public void testTransactionInsertBugRecordSeq() {
        create.transaction(config->{
            DSL.using(config).insertInto(JOOQTEST)
                    .set(getBugRecord(false)).newRecord()
                    .set(getBugRecord(true)).newRecord()
                    .set(getBugRecord(false)).newRecord()
                    .set(getRandomRecord()).execute();
        });
    }

    private JooqtestRecord getBugRecord(boolean bool){
        JooqtestRecord record = new JooqtestRecord();
        record.setInt(10L);
        if(bool){
            record.setTime(new Timestamp(1));
        }
        record.setChar("Test");
        return record;
    }

    private JooqtestRecord getRandomRecord(){
        JooqtestRecord record = new JooqtestRecord();
        maybe((s)->record.setInt(s),10L);
        maybe((s)->record.setInt1(s),10L);
        maybe((s)->record.setInt2(s),10L);
        maybe((s)->record.setInt3(s),10L);
        maybe((s)->record.setInt4(s),10L);
        maybe((s)->record.setInt5(s),10L);
        maybe((s)->record.setInt6(s),10L);
        maybe((s)->record.setInt7(s),10L);
        maybe((s)->record.setInt8(s),10L);
        maybe((s)->record.setInt9(s),10L);
        maybe((s)->record.setChar(s),"Test");
        maybe((s)->record.setChar1(s),"Test");
        maybe((s)->record.setChar2(s),"Test");
        maybe((s)->record.setChar3(s),"Test");
        maybe((s)->record.setChar4(s),"Test");
        maybe((s)->record.setChar5(s),"Test");
        maybe((s)->record.setChar6(s),"Test");
        maybe((s)->record.setChar7(s),"Test");
        maybe((s)->record.setChar9(s),"Test");
        maybe((s)->record.setCarh8(s),"Test");
        maybe((s)->record.setTime(s),new Timestamp(1));
        maybe((s)->record.setTime2(s),new Timestamp(1));
        maybe((s)->record.setTime3(s),new Timestamp(1));
        maybe((s)->record.setTime4(s),new Timestamp(1));
        maybe((s)->record.setTime5(s),new Timestamp(1));
        maybe((s)->record.setTime6(s),new Timestamp(1));
        maybe((s)->record.setTime7(s),new Timestamp(1));
        maybe((s)->record.setTime8(s),new Timestamp(1));
        maybe((s)->record.setTime9(s),new Timestamp(1));
        return record;
    }

    private <T>void maybe(Consumer<T> consumer, T t){
        if(r.nextFloat() > 0.5){
            consumer.accept(t);
        }
    }

}