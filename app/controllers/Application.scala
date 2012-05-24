package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import org.bson.types.ObjectId
import models.Service
import com.mongodb.casbah.commons.MongoDBObject

object Application extends Controller {

  def index = Action {
    Redirect(routes.ServiceController.index)
  }
}