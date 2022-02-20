package br.com.pa.lucene;

import org.apache.lucene.search.highlight.GradientFormatter;
import org.apache.lucene.search.highlight.TokenGroup;

public class CustomFormatter extends GradientFormatter {

    public CustomFormatter(float maxScore, String minForegroundColor, String maxForegroundColor, String minBackgroundColor, String maxBackgroundColor) {
        super(maxScore, minForegroundColor, maxForegroundColor, minBackgroundColor, maxBackgroundColor);
    }

    @Override
    public String highlightTerm(String originalText, TokenGroup tokenGroup) {
        if (tokenGroup.getTotalScore() == 0) return originalText;
        float score = tokenGroup.getTotalScore();
        if (score == 0) {
            return originalText;
        }

        // try to size sb correctly
        StringBuilder sb = new StringBuilder(originalText.length() + EXTRA);

        sb.append("<span style=\"");
        if (highlightForeground) {
            sb.append("color: ");
            sb.append(getForegroundColorString(score));
            sb.append("; ");
        }
        if (highlightBackground) {
            sb.append("background: ");
            sb.append(getBackgroundColorString(score));
            sb.append("; ");
        }

        sb.append("font-weight: bold; padding: 5px; ");
        sb.append("\">");
        sb.append(originalText);
        sb.append("</span>");
        return sb.toString();
    }

    // guess how much extra text we'll add to the text we're highlighting to try to avoid a
    // StringBuilder resize
    private static final String TEMPLATE =
            "<span style=\"background: #EEEEEE; color: #000000;\">...</span>";
    private static final int EXTRA = TEMPLATE.length();
}
