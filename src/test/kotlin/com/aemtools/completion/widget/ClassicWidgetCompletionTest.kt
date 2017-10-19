package com.aemtools.completion.widget

import com.aemtools.blocks.completion.CompletionBaseLightTest
import com.aemtools.completion.widget.WidgetVariantsProvider.DEFAULT_ATTRIBUTES
import com.aemtools.completion.widget.WidgetVariantsProvider.JCR_PRIMARY_TYPE_VALUES

/**
 * @author Dmytro_Troynikov
 */
class ClassicWidgetCompletionTest : CompletionBaseLightTest() {

  fun testAttributeWithXTypeUnknown() = completionTest {
    addXml("dialog.xml", """
            <item
                $CARET>
        """.inJcrRoot())
    shouldContain(DEFAULT_ATTRIBUTES)
  }

  fun testJcrPrimaryTypeValues() = completionTest {
    addXml("dialog.xml", """
            <item jcr:primaryType="$CARET"
        """.inJcrRoot())
    shouldContain(JCR_PRIMARY_TYPE_VALUES)
  }

  fun testSuggestTheFieldsResolvedFromXType() = completionTest {
    addXml("dialog.xml", """
            <item xtype="pathfield"
                $CARET
            >
        """.inJcrRoot())
    shouldContain(listOf("label", "name"), false)
  }

  fun testXtypeCompletion() = completionTest {
    addXml("dialog.xml", """
            <item jcr:primaryType="cq:widget" xtype="$CARET"
        """.inJcrRoot())

    shouldContain("annotation",
        "arraystore",
        "asseteditor",
        "assetreferencesearchdialog",
        "barchart",
        "blueprintconfig",
        "blueprintstatus",
        "box",
        "browsedialog",
        "browsefield",
        "bulkeditor",
        "bulkeditorform",
        "button",
        "buttongroup",
        "calendareventdialog",
        "calendareventpopup",
        "cartesianchart",
        "chart",
        "checkbox",
        "checkboxgroup",
        "clearcombo",
        "colorfield",
        "colorlist",
        "colormenu",
        "colorpalette",
        "columnchart",
        "combo",
        "component",
        "componentextractor",
        "componentselector",
        "componentstyles",
        "compositefield",
        "container",
        "contentfinder",
        "contentfindertab",
        "cq.compositefield",
        "cq.workflow.model.combo",
        "cq.workflow.model.selector",
        "createsitewizard",
        "createversiondialog",
        "customcontentpanel",
        "cycle",
        "dataview",
        "dataviewlens",
        "datefield",
        "datemenu",
        "datepicker",
        "datetime",
        "dialog",
        "dialogfieldset",
        "directstore",
        "displayfield",
        "editbar",
        "editor",
        "editorgrid",
        "editrollover",
        "feedimporter",
        "field",
        "fieldset",
        "fileuploaddialogbutton",
        "fileuploadfield",
        "findreplacedialog",
        "flash",
        "grid",
        "groupingstore",
        "heavymovedialog",
        "hidden",
        "historybutton",
        "html5fileuploadfield",
        "html5smartfile",
        "html5smartimage",
        "html5uploaddialog",
        "htmleditor",
        "iframedialog",
        "iframepanel",
        "inlinetextfield",
        "jsonstore",
        "label",
        "languagecopydialog",
        "lens",
        "lensdeck",
        "linechart",
        "linkchecker",
        "listview",
        "livecopyproperties",
        "lvbooleancolumn",
        "lvcolumn",
        "lvdatecolumn",
        "lvnumbercolumn",
        "mediabrowsedialog",
        "menu",
        "menubaseitem",
        "menucheckitem",
        "menuitem",
        "menuseparator",
        "menutextitem",
        "metadata",
        "monthlens",
        "multifield",
        "multislider",
        "mvt",
        "notificationinbox",
        "numberfield",
        "offlineimporter",
        "operatorselection",
        "ownerdraw",
        "paging",
        "panel",
        "paragraphreference",
        "password",
        "pathcompletion",
        "pathfield",
        "piechart",
        "pivotgrid",
        "progress",
        "propertygrid",
        "propgrid",
        "querybuilder",
        "quicktip",
        "radio",
        "radiogroup",
        "recurrencerule",
        "referencesdialog",
        "restoretreedialog",
        "restoreversiondialog",
        "richtext",
        "rolloutplan",
        "rolloutwizard",
        "searchfield",
        "selection",
        "showhide",
        "sidekick",
        "siteadmin",
        "siteadmingrid",
        "siteadminsearchpanel",
        "siteimporter",
        "sizefield",
        "slider",
        "sliderfield",
        "slideshow",
        "smartfile",
        "smartimage",
        "spacer",
        "spinner",
        "splitbutton",
        "stackedbarchart",
        "stackedcolumnchart",
        "static",
        "statistics",
        "store",
        "suggestfield",
        "switcher",
        "tableedit",
        "tableedit2",
        "tabpanel",
        "tags",
        "textarea",
        "textbutton",
        "textfield",
        "thumbnail",
        "timefield",
        "tip",
        "titleseparator",
        "toolbar",
        "tooltip",
        "treegrid",
        "treepanel",
        "trigger",
        "uploaddialog",
        "userinfo",
        "viewport",
        "window",
        "xmlstore")
  }

  private fun String.inJcrRoot(): String = """
        <?xml version="1.0" encoding="UTF-8"?>
        <jcr:root xmlns:cq="http://www.day.com/jcr/cq/1.0"
                  xmlns:jcr="http://www.jcp.org/jcr/1.0">
        $this
        </jcr:root>
    """

}
