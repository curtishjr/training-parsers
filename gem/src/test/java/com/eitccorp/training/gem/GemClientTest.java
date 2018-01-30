package com.eitccorp.training.gem;

import bdp.ingest.test.TestUtils;
import gem.domain.Entity;
import gem.index.EntityIndexer;
import gem.index.IndexQuery;
import gem.index.accumulo.AccumuloIndexStore;
import gem.index.rya.SlidingWindowEntityIndexer;
import gem.rya.store.service.impl.RyaEntityService;
import gem.service.Auths;
import gem.service.IndexEntityService;
import junit.framework.Assert;
import mvm.rya.accumulo.AccumuloRyaDAO;
import mvm.rya.accumulo.query.AccumuloRyaQueryEngine;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.calrissian.mango.collect.CloseableIterable;
import org.calrissian.mango.collect.FluentCloseableIterable;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class GemClientTest {

    @Test
    public void testGemQueryByTerm() throws Exception{
        Connector connector = new MockInstance().getConnector("root", new PasswordToken(""));

        AccumuloRyaDAO ryaDAO = new AccumuloRyaDAO();
        ryaDAO.setConnector(connector);
        ryaDAO.setQueryEngine(new AccumuloRyaQueryEngine(connector));
        ryaDAO.init();

        RyaEntityService ryaEntityService = new RyaEntityService(ryaDAO);
        EntityIndexer entityIndexer = new SlidingWindowEntityIndexer(new AccumuloIndexStore(connector));
        IndexEntityService entityService = new IndexEntityService(ryaEntityService, entityIndexer);

        entityService.saveEntities(newArrayList(TestUtils.parseGemFile("src/test/resources/config", "port-enrichment", "src/test/resources/service-names-port-numbers.csv")));

        CloseableIterable<Entity> entities = entityService.search(new IndexQuery("tcp"), new Auths("U"));

        // Example of utilities in Mango for CloseableIterables.
        List<Entity> entityList =
                FluentCloseableIterable.from(entities)
                        .limit(200)  // Limit to first 100 entities
                        .autoClose() // Automatically closes when the underlying iterator has been exhausted
                        .toList();   // Accumulates the data into a list, exhausting the underlying iterator

        Assert.assertEquals(127, entityList.size());
    }
}
