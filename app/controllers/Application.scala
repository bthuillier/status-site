package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import org.bson.types.ObjectId
import models.Service
import com.mongodb.casbah.commons.MongoDBObject

object Application extends Controller {
  
  val serviceForm = Form(
    mapping(
      "id" -> ignored(new ObjectId()),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "status" -> nonEmptyText
    )(Service.apply)(Service.unapply)
  )
  def index = Action {
    Redirect(routes.Application.services)
  }
  
  def services = Action {
    Ok(views.html.index(Service.findAll().toList, serviceForm))
  }
  
  def newService = Action { implicit request =>
  serviceForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Service.findAll().toList, errors)),
    service => {
      Service.insert(service)
      Redirect(routes.Application.services)
    }
  )
}
  
  def deleteService(id: ObjectId) = Action {
    Service.remove(MongoDBObject("_id" -> id))
    Redirect(routes.Application.services)
  }
  
}