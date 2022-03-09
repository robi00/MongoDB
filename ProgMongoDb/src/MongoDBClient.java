

import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class MongoDBClient {

	public static void main(String[] args) throws Exception {

		//Creation Mongo Client
		MongoClient mongo = new MongoClient("localhost", 27017);

		//Credential for connection to MongoDB
		MongoCredential credential;
		credential = MongoCredential.createCredential("MongoUser", "myMongoDB", "password1234".toCharArray());
		System.out.println("Connection to database MongoDB successful");

		//Get the database just created
		MongoDatabase mongoDatabase = mongo.getDatabase("myMongoDB");

		//verify credentials for testing
		System.out.println("Credenziali: "+ credential);

		//Create collections (one for each account)
		int n = 1;
		while (n<6) {
			//check if the collection exists
			MongoCollection<Document> collection =  mongoDatabase.getCollection("Account"+n);
			if(collection == null) {
			mongoDatabase.createCollection("Account"+n);
			System.out.println("Account"+n);
			//get the collection just created
			collection = mongoDatabase.getCollection("Account"+n);

			System.out.println("Collection: Account"+n);
			n++;
			}
			else {
				System.out.println("Collection già esistente");
				n++;
			}
		}


		//Insert a new document?
		Scanner sc = new Scanner(System.in);

		System.out.println("what operation do you want to carry out? (store/get)");
		String read1 = sc.nextLine();

		if (read1.equals("store")) {
			boolean flag = true;
			while(flag == true) {
				System.out.println("Account's address: ");
				String address = sc.nextLine();
				System.out.println("Transaction hash: ");
				String hash = sc.nextLine();
				System.out.println("From: ");
				String from = sc.nextLine();
				System.out.println("To: ");
				String to = sc.nextLine();
				System.out.println("Gas price: ");
				String gasPrice = sc.nextLine();
				System.out.println("Gas used: ");
				String gasUsed = sc.nextLine();
				System.out.println("Time stamp: ");
				String timeStamp = sc.nextLine();
				System.out.println("Value: ");
				String value = sc.nextLine();
				System.out.println("Contract address: ");
				String contractAddress = sc.nextLine();
				System.out.println("Token symbol: ");
				String tokenSymbol = sc.nextLine();
				System.out.println("which account performed this transaction? (Accountx) ");
				String acc = sc.nextLine();
				System.out.println("Transaction informations: " + address + ", " + hash + ", " + from + ", " + to + ", " + gasPrice + ", " + gasUsed + ", " 
						+ timeStamp + ", " + value + ", " + contractAddress + ", " + tokenSymbol );
				Document document = new Document ("address", address)
						.append("hash", ""+hash)
						.append("from", ""+from)
						.append("to", ""+to)
						.append("gasPrice", ""+gasPrice)
						.append("gasUsed", ""+gasUsed)
						.append("timeStamp", ""+timeStamp)
						.append("value", ""+value)
						.append("contractAddress", ""+contractAddress)
						.append("tokenSymbol", ""+tokenSymbol);


				//inserimento del documento del account corrispondente
				MongoCollection<Document> store =  mongoDatabase.getCollection(""+acc);
				store.insertOne(document);
				System.out.println("document added to the account "+acc);
				System.out.println("do you want to store another transation? (yes/no)");
				String response = sc.nextLine();
				if (response.equals("no")) {
					flag = false;
				}
			}

		}
		else if(read1.equals("get")) {
			boolean flag = true;
			while (flag == true) {
				System.out.println ("which account made the transaction you are looking for? (Accountx)");
				String acc = sc.nextLine();
				System.out.println("Hash of the transaction you want to search for: ");
				String search = sc.nextLine();

				MongoCollection<Document> get =  mongoDatabase.getCollection(acc);

				// return all documents with only the hash field
				Bson filter = Filters.eq("hash", search);
				for(Document d : get.find(filter)) {
					System.out.println(d);
				}
				System.out.println("do you want to get another transation? (yes/no)");
				String response = sc.nextLine();
				if (response.equals("no")) {
					flag = false;
				}
			}
		}

		mongo.close();
		sc.close();
	}

}


