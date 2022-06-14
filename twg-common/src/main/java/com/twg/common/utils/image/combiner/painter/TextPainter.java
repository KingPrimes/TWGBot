package com.twg.common.utils.image.combiner.painter;


import com.twg.common.utils.image.combiner.element.CombineElement;
import com.twg.common.utils.image.combiner.element.TextElement;
import com.twg.common.utils.image.combiner.enums.Direction;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public class TextPainter implements IPainter {
    public TextPainter() {
    }

    public void draw(Graphics2D g, CombineElement element, int canvasWidth) {
        TextElement textElement = (TextElement) element;
        java.util.List<TextElement> textLineElements = new ArrayList();
        ((java.util.List) textLineElements).add(textElement);
        if (textElement.isAutoBreakLine()) {
            textLineElements = textElement.getBreakLineElements();
        }

        for (int i = 0; i < ((java.util.List) textLineElements).size(); ++i) {
            TextElement firstLineElement = (TextElement) ((java.util.List) textLineElements).get(0);
            TextElement currentLineElement = (TextElement) ((List) textLineElements).get(i);
            g.setFont(currentLineElement.getFont());
            g.setColor(currentLineElement.getColor());
            if (currentLineElement.isCenter()) {
                if (i == 0) {
                    currentLineElement.setX((canvasWidth - currentLineElement.getWidth()) / 2);
                } else {
                    switch (textElement.getLineAlign()) {
                        case Left:
                            currentLineElement.setX(firstLineElement.getX());
                            break;
                        case Center:
                            currentLineElement.setX((canvasWidth - currentLineElement.getWidth()) / 2);
                            break;
                        case Right:
                            currentLineElement.setX(firstLineElement.getX() + firstLineElement.getWidth() - currentLineElement.getWidth());
                    }
                }
            } else if (i == 0) {
                if (currentLineElement.getDirection() == Direction.RightLeft) {
                    currentLineElement.setX(currentLineElement.getX() - currentLineElement.getWidth());
                } else if (currentLineElement.getDirection() == Direction.CenterLeftRight) {
                    currentLineElement.setX(currentLineElement.getX() - currentLineElement.getWidth() / 2);
                }
            } else {
                switch (textElement.getLineAlign()) {
                    case Left:
                        currentLineElement.setX(firstLineElement.getX());
                        break;
                    case Center:
                        currentLineElement.setX(firstLineElement.getX() + (firstLineElement.getWidth() - currentLineElement.getWidth()) / 2);
                        break;
                    case Right:
                        currentLineElement.setX(firstLineElement.getX() + firstLineElement.getWidth() - currentLineElement.getWidth());
                }
            }

            if (currentLineElement.getRotate() != null) {
                g.rotate(Math.toRadians((double) currentLineElement.getRotate()), (double) (currentLineElement.getX() + currentLineElement.getWidth() / 2), (double) currentLineElement.getDrawY());
            }

            g.setComposite(AlphaComposite.getInstance(3, currentLineElement.getAlpha()));
            if (currentLineElement.isStrikeThrough()) {
                AttributedString as = new AttributedString(currentLineElement.getText());
                as.addAttribute(TextAttribute.FONT, currentLineElement.getFont());
                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, currentLineElement.getText().length());
                g.drawString(as.getIterator(), currentLineElement.getX(), currentLineElement.getDrawY());
            } else {
                g.drawString(currentLineElement.getText(), currentLineElement.getX(), currentLineElement.getDrawY());
            }

            if (currentLineElement.getRotate() != null) {
                g.rotate(-Math.toRadians((double) currentLineElement.getRotate()), (double) (currentLineElement.getX() + currentLineElement.getWidth() / 2), (double) currentLineElement.getDrawY());
            }
        }

    }
}

