package org.zer0.demo.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class TestElasticSearch {

	public static void main(String[] args) {
		TestElasticSearch testElastic=new TestElasticSearch();
		testElastic.crearIndice();
	}
	
	private void crearIndice() {
		try {	
			TransportClient cliente=new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"),9300));
			String index="personas";
			cliente.admin().indices().create(new CreateIndexRequest(index)).actionGet();
			cliente.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private void poblarIndice(TransportClient cliente,List<Persona> personas,String index) {
		BulkRequestBuilder builder=cliente.prepareBulk();
		personas.forEach(
						(persona)->{builder.add(new IndexRequest(index, "persona").source(persona));}
					);
		BulkResponse response=builder.get();
	}
	
	private void buscarInformacion(TransportClient cliente) {
		SearchResponse respuesta=cliente.prepareSearch("personas")
								.setQuery(QueryBuilders.termQuery("nombres", "jose"))
								.setSize(30)
								.get();
		
	}
	
}
