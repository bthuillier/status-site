package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import org.bson.types.ObjectId
import models.Service
import com.mongodb.casbah.commons.MongoDBObject

object ServiceController extends Controller {
  val serviceForm = Form(
    mapping(
      "id" -> ignored(new ObjectId()),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "status" -> nonEmptyText)(Service.apply)(Service.unapply))

  def index = Action {
    Ok(views.html.index(Service.findAll().toList, serviceForm))
  }

  def create = Action {
    Ok(views.html.create(serviceForm))
  }

  def save = Action { implicit request =>
    serviceForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Service.findAll().toList, errors)),
      service => {
        Service.save(service)
        Redirect(routes.ServiceController.index)
      })
  }

  def delete(id: ObjectId) = Action {
    Service.remove(MongoDBObject("_id" -> id))
    Redirect(routes.ServiceController.index)
  }
  
  def edit(id: ObjectId) = Action { 
    Service.findOneByID(id).map { service =>
      Ok(views.html.edit(id, serviceForm.fill(service)))
    }.getOrElse(NotFound)
  }

  def update(id: ObjectId) = Action { implicit request =>
    serviceForm.bindFromRequest.fold(
      error => BadRequest(views.html.edit(id, error)),
      service => {
        Service.save(service.copy(id = id))
        Redirect(routes.ServiceController.index)
      }
    )
  }
  
  def show(name: String) = TODO
}