/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zeppelin.markdown;

import org.apache.zeppelin.interpreter.InterpreterResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MarkdownTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }


  String tbl = "  First   | Second \n" +
      "  ------------- | -------------\n" +
      "  Content Cell  | Content Cell\n" +
      "  Content Cell  | Content Cell";
  String link = "[like this](http://someurl)";

  @Test
  public void testMarkdown() {
    Markdown md = new Markdown(new Properties());
    md.open();

    InterpreterResult tblRe = md.interpret(tbl, null);
    assertEquals("<table class=\"table table-bordered table-striped table-condensed\">\n" +
        "  <thead>\n" +
        "    <tr>\n" +
        "      <th>First </th>\n" +
        "      <th>Second</th>\n" +
        "    </tr>\n" +
        "  </thead>\n" +
        "  <tbody>\n" +
        "    <tr>\n" +
        "      <td>Content Cell </td>\n" +
        "      <td>Content Cell</td>\n" +
        "    </tr>\n" +
        "    <tr>\n" +
        "      <td>Content Cell </td>\n" +
        "      <td>Content Cell</td>\n" +
        "    </tr>\n" +
        "  </tbody>\n" +
        "</table>", tblRe.message());
    System.out.println(tblRe.message());

    InterpreterResult linkRe = md.interpret(link, null);
    assertEquals("<p><a href=\"http://someurl\">like this</a></p>", linkRe.message());
    System.out.println(linkRe.message());
  }


  @Test
  public void testPegDownProcessor() {
    PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
    String re = processor.markdownToHtml(tbl);
    System.out.println(re);
  }

  @Test
  public void testPegDownProcessor2() {
    PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
    RootNode astRoot = processor.parseMarkdown(tbl.toCharArray());


    String re = new GIOToHtmlSerializer(new LinkRenderer()).toHtml(astRoot);
    System.out.println(re);
  }

}
