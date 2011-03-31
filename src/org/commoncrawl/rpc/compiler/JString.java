/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.commoncrawl.rpc.compiler;

/**
 */
public class JString extends JCompType {

  class JavaString extends JavaCompType {

    JavaString() {
      super("TextBytes", "TextBytes", "TextBytes");
    }

    @Override
    void genClone(CodeBuffer cb, String type, String targetField,
        String sourceField) {
      cb.append(targetField + "= (TextBytes)" + sourceField + ".clone();\n");
    }

    @Override
    void genClearMethod(CodeBuffer cb, String fname) {
      // TODO: IS THIS THE MOST EFFICIENT WAY TO CLEAR A STRING
      cb.append(fname + ".clear();\n");
    }

    @Override
    void genDecl(CodeBuffer cb, String fname) {
      cb.append("private " + getType() + " " + fname + "=  new TextBytes();\n");
    }

    @Override
    void genHashCode(CodeBuffer cb, String fname) {
      cb.append("result = MurmurHash.hash(" + fname + ".getBytes()," + fname
          + ".getOffset()," + fname + ".getLength(),result);\n");
    }

    @Override
    void genReadMethod(CodeBuffer cb, String fname, String tag, boolean decl) {
      if (decl) {
        cb.append(getType() + " " + fname + " = new TextBytes();\n");
      }
      cb
          .append("decoder.read" + getMethodSuffix() + "(input," + fname
              + ");\n");
    }

    @Override
    void genGetSet(CodeBuffer cb, String fname, boolean trackDirtyFields) {
      cb.append("public TextBytes get" + toCamelCase(fname)
          + "AsTextBytes() {\n");
      cb.append("return " + fname + ";\n");
      cb.append("}\n");

      cb.append("public String get" + toCamelCase(fname) + "() {\n");
      cb.append("return " + fname + ".toString();\n");
      cb.append("}\n");
      cb.append("public void set" + toCamelCase(fname) + "( String " + fname
          + ") {\n");
      if (trackDirtyFields) {
        cb.append("__validFields.set(Field_" + fname.toUpperCase() + ");\n");
      }
      cb.append("this." + fname + ".set(" + fname + ");\n");
      cb.append("}\n");
    }
  }

  /** Creates a new instance of JString */
  public JString() {
    setJavaType(new JavaString());
  }

  String getSignature() {
    return "s";
  }

  boolean isComparable() {
    return true;
  }
}
