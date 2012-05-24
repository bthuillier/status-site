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
      "status" -> nonEmptyText)(Service.apply)(Service.unapply))
  def index = Action {
    Redirect(routes.Application.services)
  }

  def services = Action {
    Ok(views.html.index(Service.findAll().toList, serviceForm))
  }

  def createService = Action {
    Ok(views.html.create(serviceForm))
  }

  def newService = Action { implicit request =>
    serviceForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Service.findAll().toList, errors)),
      service => {
        Service.save(service)
        Redirect(routes.Application.services)
      })
  }

  def deleteService(id: ObjectId) = Action {
    Service.remove(MongoDBObject("_id" -> id))
    Redirect(routes.Application.services)
  }
  
  def editService(id: ObjectId) = Action { 
    Service.findOneByID(id).map { service =>
      Ok(views.html.edit(id, serviceForm.fill(service)))
    }.getOrElse(NotFound)
  }

  def updateService(id: ObjectId) = Action { implicit request =>
    serviceForm.bindFromRequest.fold(
      error => BadRequest(views.html.edit(id, error)),
      service => {
        Service.save(service.copy(id = id))
        Redirect(routes.Application.services)
      }
    )
  } 
}