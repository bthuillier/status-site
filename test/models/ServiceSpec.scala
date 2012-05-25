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
  
  val serviceId  = new ObjectId("4f7e12bf7f25471356f51e39")  
  val statusSite = Service(serviceId, "status-site", "the status site", "ok")
  step {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        Service.remove(MongoDBObject.empty)

        Service.insert(statusSite)

      }
  }
  
  "Application" should {
    "redirect to the service list on / " in {
      val result = controllers.Application.index(FakeRequest())
      status(result) must equalTo(SEE_OTHER)
      redirectLocation(result) must beSome.which(_ == "/services")      
    }
    
     "list services on the the first page" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val result = controllers.ServiceController.index(FakeRequest())
        status(result) must equalTo(OK)
        contentAsString(result) must contain("1 service(s)")
      }
    }
     
     "create a new service" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val badResult = controllers.ServiceController.save(FakeRequest())
        status(badResult) must equalTo(BAD_REQUEST)
        val result = controllers.ServiceController.save(
          FakeRequest().withFormUrlEncodedBody("name" -> "FooBar", "description" -> "2011-12-24", "status" -> "4f7dc7c47f25471356f51366")
        )
        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_ == "/services")
        val list = controllers.ServiceController.index(FakeRequest())

        status(list) must equalTo(OK)
        contentAsString(list) must contain("2 service(s)")        
        
      }       
     }

     "edit a service" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val badResult = controllers.ServiceController.update(serviceId)(FakeRequest())
        status(badResult) must equalTo(BAD_REQUEST)
        val result = controllers.ServiceController.update(serviceId)(
          FakeRequest().withFormUrlEncodedBody("name" -> statusSite.name, "description" -> "2011-12-24", "status" -> "4f7dc7c47f25471356f51366")
        )
        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_ == "/services")
        val list = controllers.ServiceController.index(FakeRequest())

        status(list) must equalTo(OK)
        contentAsString(list) must contain("2 service(s)")        
        
      }       
     }  

     "show a service" in {
      running(FakeApplication(additionalConfiguration = mongoTestDatabase())) {
        val badResult = controllers.ServiceController.show("azerty")(FakeRequest())
        status(badResult) must equalTo(NOT_FOUND)
        val result = controllers.ServiceController.show(statusSite.name)(FakeRequest())
        status(result) must equalTo(OK)

        contentAsString(result) must contain(statusSite.name)        
        
      }       
     }      
  }
}