package org.apache.zeppelin.markdown;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.SuperNode;

/**
 *  * Created by hj on 16/7/20.
 *   */
public class GIOToHtmlSerializer extends ToHtmlSerializer {
  public GIOToHtmlSerializer(LinkRenderer linkRenderer) {
    super(linkRenderer);
  }

  @Override
  protected void printIndentedTag(SuperNode node, String tag) {
    if ("table".equals(tag)) {
      String tableStyle = " class=\"table table-bordered table-striped table-condensed\"";
      printer.println().print('<').print(tag).print(tableStyle).print('>').indent(+2);
    } else {
      printer.println().print('<').print(tag).print('>').indent(+2);
    }
    visitChildren(node);
    printer.indent(-2).println().print('<').print('/').print(tag).print('>');
  }
}
