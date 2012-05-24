package models

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class ServiceSpec extends Specification {
  import models._
  import com.mongodb.casbah.Imports._
  
  def mongoTestDatabase() = {
    Map("mongo.default.db" -> "computer-database-test")
  }  
  
  val serviceId = new ObjectId("4f7e12bf7f25471356f51e39")  

  step {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        Service.remove(MongoDBObject.empty)

        val statusSite = Service(serviceId, "status-site", "the status site", "ok")
        Service.insert(statusSite)

      }
  }
  
  "Application" should {
    "redirect to the service list on /" in {
      val result = controllers.Application.index(FakeRequest())
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome.which(_ == "/services")      
    }
    
     "list services on the the first page" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val result = controllers.Application.services(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("1 service(s)")
      }
    }   
  }
}