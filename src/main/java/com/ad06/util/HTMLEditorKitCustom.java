/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ad06.util;

import javax.swing.SizeRequirements;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.View;
import static javax.swing.text.View.GoodBreakWeight;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;

/**
 *
 * @author Manuel
 */
public class HTMLEditorKitCustom extends HTMLEditorKit {

    public HTMLEditorKitCustom() {

        getStyleSheet().addRule("p {font-family: verdana; font-size: 20px; color: white;}");

    }

    @Override
    public ViewFactory getViewFactory() {

        return new HTMLFactory() {
            @Override
            public View create(Element e) {
                View v = super.create(e);
                
                AttributeSet attrs = e.getAttributes();

                Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);

                Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);

                HTML.Tag kind = (o instanceof HTML.Tag ? (HTML.Tag) o : null);

                if (v instanceof InlineView && kind != HTML.Tag.BR) {
                    return new InlineView(e) {
                        @Override
                        public int getBreakWeight(int axis, float pos, float len) {
                            return GoodBreakWeight;
                        }

                        @Override
                        public View breakView(int axis, int p0, float pos, float len) {
                            if (axis == View.X_AXIS) {
                                checkPainter();
                                int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
                                if (p0 == getStartOffset() && p1 == getEndOffset()) {
                                    return this;
                                }
                                return createFragment(p0, p1);
                            }
                            return this;
                        }
                    };
                } else if (v instanceof ParagraphView) {
                    return new ParagraphView(e) {
                        @Override
                        protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
                            if (r == null) {
                                r = new SizeRequirements();
                            }
                            float pref = layoutPool.getPreferredSpan(axis);
                            float min = layoutPool.getMinimumSpan(axis);
                            // Don't include insets, Box.getXXXSpan will include them. 
                            r.minimum = (int) min;
                            r.preferred = Math.max(r.minimum, (int) pref);
                            r.maximum = Integer.MAX_VALUE;
                            r.alignment = 0.5f;
                            return r;
                        }

                    };
                }
                return v;
            }
        };
    }

}
