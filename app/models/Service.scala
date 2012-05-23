package models
import play.api.Play.current
import com.novus.salat._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._

case class Service(
    id: ObjectId = new ObjectId,
    name: String,
    description: String,
    status: String)
    
object Service extends ModelCompanion[Service, ObjectId] {
	val collection = mongoCollection("services")
	val dao = new SalatDAO[Service, ObjectId](collection = collection) {}
	def findOneByName(name: String): Option[Service] = dao.findOne(MongoDBObject("name" -> name))
  
}