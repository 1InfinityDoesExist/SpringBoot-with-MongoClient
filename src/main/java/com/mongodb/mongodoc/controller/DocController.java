package com.mongodb.mongodoc.controller;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("/")
public class DocController {

	@GetMapping
	public ResponseEntity<?> renderDoc() {

		String mongoURL = "mongodb://localhost:27017/iam";
		try (MongoClient mongoClient = MongoClients.create(mongoURL)) {
			MongoDatabase database = mongoClient.getDatabase("iam");
			MongoCollection<Document> collection = database.getCollection("product");

			MongoCollection<Document> collection_dummy = database.getCollection("product_dummy");
			FindIterable<Document> col = collection.find();
			for (Document doc : col) {

				log.info("------ID : {}", (String) doc.get("_id"));

				String jsonString = doc.toJson();
				// update the jsonString in any way you like.
				Document updatedDoc = Document.parse(jsonString);

				collection_dummy.insertOne(updatedDoc); // insert it to any collection;
			}

		} catch (final Exception ex) {
			log.error("-----Error connecting to  the database");
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute("resposne", "Success"));

	}

}
