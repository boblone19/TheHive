package org.thp.thehive.connector.cortex.controllers.v0

import io.scalaland.chimney.dsl._
import org.thp.cortex.dto.v0.{InputReportTemplate, OutputReportTemplate}
import org.thp.scalligraph.Output
import org.thp.scalligraph.models.Entity
import org.thp.scalligraph.query.{PublicProperty, PublicPropertyListBuilder}
import org.thp.thehive.connector.cortex.models.{ReportTemplate, ReportType}
import org.thp.thehive.connector.cortex.services.ReportTemplateSteps

import scala.language.implicitConversions

object ReportTemplateConversion {

  implicit def toOutputReportTemplate(j: ReportTemplate with Entity): Output[OutputReportTemplate] =
    Output[OutputReportTemplate](
      j.into[OutputReportTemplate]
        .withFieldComputed(_.analyzerId, _.workerId)
        .withFieldComputed(_.id, _._id)
        .withFieldComputed(_.content, _.content)
        .withFieldComputed(_.reportType, _.reportType.toString)
        .transform
    )

  val reportTemplateProperties: List[PublicProperty[_, _]] =
    PublicPropertyListBuilder[ReportTemplateSteps]
      .property[String]("analyzerId")(_.rename("workerId").updatable)
      .property[String]("reportType")(_.simple.updatable)
      .property[String]("content")(_.simple.updatable)
      .build

  implicit def fromInputReportTemplate(inputReportTemplate: InputReportTemplate): ReportTemplate =
    inputReportTemplate
      .into[ReportTemplate]
      .withFieldComputed(_.reportType, r => ReportType.withName(r.reportType))
      .withFieldComputed(_.workerId, _.analyzerId)
      .withFieldComputed(_.content, _.content)
      .transform
}
