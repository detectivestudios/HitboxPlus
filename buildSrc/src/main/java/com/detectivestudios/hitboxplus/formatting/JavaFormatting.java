/*
 * Hitbox+ by DetectiveStudios
 * Copyright (c) 2026 DetectiveStudios and the respective contributors.
 *
 * Licensed under the DetectiveStudios Source-Available Contribution License.
 * See LICENSE.txt for the complete terms and third-party exceptions.
 *
 * Contribution forks and pull requests to the official GitHub repository are
 * permitted. Reuse in other projects and redistribution require separate written
 * permission, except as expressly provided in the license.
 */

package com.detectivestudios.hitboxplus.formatting;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class JavaFormatting {

  public static String format(String source) {
    var compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      throw new IllegalStateException("Java formatting requires a JDK.");
    }
    var diagnostics = new DiagnosticCollector<JavaFileObject>();
    var input =
        new SimpleJavaFileObject(
            URI.create("string:///FormattingSource.java"), JavaFileObject.Kind.SOURCE) {

          @Override
          public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return source;
          }
        };
    var edits = new ArrayList<Edit>();
    try (var manager = compiler.getStandardFileManager(diagnostics, null, null)) {
      var task =
          (JavacTask)
              compiler.getTask(
                  null, manager, diagnostics, List.of("-proc:none"), null, List.of(input));
      var units = task.parse();
      if (diagnostics.getDiagnostics().stream()
          .anyMatch(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR)) {
        throw new IllegalArgumentException(
            "Cannot format invalid Java source: " + diagnostics.getDiagnostics());
      }
      var positions = Trees.instance(task).getSourcePositions();
      for (var unit : units) {
        new TreeScanner<Void, Void>() {

          @Override
          public Void visitClass(ClassTree type, Void unused) {
            int start = (int) positions.getStartPosition(unit, type);
            int end = (int) positions.getEndPosition(unit, type);
            int body = JavaFormatting.bodyStart(source, start, end);
            if (body >= 0
                && !source.substring(body + 1, end - 1).isBlank()
                && !source.startsWith("\n\n", body + 1)) {
              edits.add(new Edit(body + 1, body + 1, "\n"));
            }
            boolean mixin =
                type.getModifiers().getAnnotations().stream()
                    .anyMatch(
                        annotation ->
                            annotation.getAnnotationType().toString().equals("Mixin")
                                || annotation
                                    .getAnnotationType()
                                    .toString()
                                    .equals("org.spongepowered.asm.mixin.Mixin"));
            if (mixin && source.contains("org.spongepowered.asm.mixin.Mixin")) {
              for (Tree member : type.getMembers()) {
                if (!(member instanceof MethodTree method) || method.getBody() == null) {
                  continue;
                }
                int signature = (int) positions.getStartPosition(unit, method);
                for (var annotation : method.getModifiers().getAnnotations()) {
                  int annotationStart = (int) positions.getStartPosition(unit, annotation);
                  int annotationEnd = (int) positions.getEndPosition(unit, annotation);
                  JavaFormatting.compact(source, annotationStart, annotationEnd, edits);
                  signature = annotationEnd;
                }
                while (signature < source.length()
                    && Character.isWhitespace(source.charAt(signature))) {
                  signature++;
                }
                int methodBody = (int) positions.getStartPosition(unit, method.getBody());
                JavaFormatting.compact(source, signature, methodBody + 1, edits);
              }
            }
            return super.visitClass(type, unused);
          }
        }.scan(unit, null);
      }
    } catch (IOException exception) {
      throw new IllegalStateException("Cannot parse Java source", exception);
    }
    var result = new StringBuilder(source);
    edits.sort(Comparator.comparingInt(Edit::start).reversed());
    for (var edit : edits) {
      result.replace(edit.start(), edit.end(), edit.text());
    }
    return result.toString();
  }

  private static int bodyStart(String source, int start, int end) {
    int parentheses = 0;
    for (int index = start; index >= 0 && index < end; index++) {
      char current = source.charAt(index);
      if (source.startsWith("//", index)) {
        int newline = source.indexOf('\n', index + 2);
        index = newline < 0 ? end : newline;
      } else if (source.startsWith("/*", index)) {
        int close = source.indexOf("*/", index + 2);
        index = close < 0 ? end : close + 1;
      } else if (current == '"' || current == '\'') {
        String delimiter = source.startsWith("\"\"\"", index) ? "\"\"\"" : String.valueOf(current);
        index += delimiter.length();
        while (index < end && !source.startsWith(delimiter, index)) {
          index += source.charAt(index) == '\\' ? 2 : 1;
        }
        index += delimiter.length() - 1;
      } else if (current == '(') {
        parentheses++;
      } else if (current == ')') {
        parentheses--;
      } else if (current == '{' && parentheses == 0) {
        return index;
      }
    }
    return -1;
  }

  private static void compact(String source, int start, int end, List<Edit> edits) {
    String fragment = source.substring(start, end);
    // Joining comments or text blocks can change their meaning.
    if (fragment.contains("//") || fragment.contains("/*") || fragment.contains("\"\"\"")) {
      return;
    }
    var result = new StringBuilder();
    char quote = 0;
    boolean space = false;
    for (int index = 0; index < fragment.length(); index++) {
      char current = fragment.charAt(index);
      if (quote != 0) {
        result.append(current);
        if (current == '\\' && index + 1 < fragment.length()) {
          result.append(fragment.charAt(++index));
        } else if (current == quote) {
          quote = 0;
        }
      } else if (Character.isWhitespace(current)) {
        space = true;
      } else {
        if (space
            && !result.isEmpty()
            && result.charAt(result.length() - 1) != '('
            && current != ')') {
          result.append(' ');
        }
        space = false;
        result.append(current);
        if (current == '"' || current == '\'') {
          quote = current;
        }
      }
    }
    edits.add(new Edit(start, end, result.toString()));
  }

  private record Edit(int start, int end, String text) {}
}
