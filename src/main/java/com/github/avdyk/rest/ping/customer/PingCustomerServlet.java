package com.github.avdyk.rest.ping.customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;

/**
 * Ping Customer Servlet.
 *
 * @author <a href="mailto:arnaud.vandyck@staff.voo.be">Arnaud Vandyck</a>
 * @since 1.0.0, 1/15/16.
 */
@WebServlet(value="/rest/client/usage", name="pingcustomerservlet")
public class PingCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String clientIds = req.getParameter("clientIds");
        if (clientIds != null) {
            final StringBuilder responseString = new StringBuilder("{  \"usages\": [\n");
            final StringTokenizer tokenizer = new StringTokenizer(clientIds, ",");
            if (tokenizer.hasMoreTokens()) {
                responseString.append(createQuota(tokenizer.nextToken()));
            }
            while (tokenizer.hasMoreTokens()) {
                responseString
                    .append(",\n")
                    .append(createQuota(tokenizer.nextToken()));
            }
            responseString.append("\n  ]\n}\n");
            resp.setContentType("application/json");
            resp.getWriter().print(responseString.toString());
        } else {
            // problem...
            throw new IllegalArgumentException("No clientIds found");
        }
    }

    private String createQuota(final String id) {
        StringBuilder qString = new StringBuilder("    {\n");
        qString.append("      \"clientId\": \"").append(id).append("\",\n");
        qString.append("      \"storageMb\": ").append(createBigDecimal()).append('\n');
        qString.append("    }");
        log("Recieved id: " + id + "; created quota: " + qString.toString());
        return qString.toString();
    }

    private String createBigDecimal() {
        return new BigDecimal(Integer.toString((int)(Math.random() * 42))
            + "."
            + Integer.toString((int)(Math.random() * 100))).toString();
    }
}
