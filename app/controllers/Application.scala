package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import org.bson.types.ObjectId
import models.Service

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
    Ok("Hello world")
  }
  
  def services = TODO
  
  def newService = TODO
  
  def deleteService(name: String) = TODO
  
}