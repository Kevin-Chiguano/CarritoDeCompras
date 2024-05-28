package org.compras.compras;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/factura")
public class Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        HttpSession session = req.getSession();

        // Obtener los parámetros del formulario
        String nombreProducto = req.getParameter("nombreProducto");
        double valorUnitario = Double.parseDouble(req.getParameter("valorUnitario"));
        int cantidad = Integer.parseInt(req.getParameter("cantidad"));

        // Calcular el valor total y el IVA del producto actual
        double valorTotal = valorUnitario * cantidad;
        double iva = valorTotal * 0.15;
        double totalConIva = valorTotal + iva;

        // Crear un nuevo producto
        Producto producto = new Producto(nombreProducto, valorUnitario, cantidad, valorTotal, iva, totalConIva);

        // Obtener la lista de compras de la sesión
        List<Producto> compras = (List<Producto>) session.getAttribute("compras");
        if (compras == null) {
            compras = new ArrayList<>();
        }
        // Agregar el nuevo producto a la lista
        compras.add(producto);

        // Guardar la lista de compras en la sesión
        session.setAttribute("compras", compras);

        // Generar la factura
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Factura</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; }");
        out.println(".container { width: 50%; margin: 0 auto; text-align: center; }");
        out.println(".factura { border: 1px solid #000; padding: 20px; }");
        out.println("table { width: 100%; border-collapse: collapse; }");
        out.println("th, td { border: 1px solid #000; padding: 8px; text-align: left; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<div class='factura'>");
        out.println("<h1>Factura</h1>");
        out.println("<table>");
        out.println("<tr><th>Producto</th><th>Valor Unitario</th><th>Cantidad</th><th>Valor Total</th><th>IVA</th><th>Total con IVA</th></tr>");

        double totalGeneral = 0;
        double totalIva = 0;
        double totalConIvaGeneral = 0;

        for (Producto p : compras) {
            out.println("<tr>");
            out.println("<td>" + p.getNombre() + "</td>");
            out.println("<td>$" + String.format("%.2f", p.getValorUnitario()) + "</td>");
            out.println("<td>" + p.getCantidad() + "</td>");
            out.println("<td>$" + String.format("%.2f", p.getValorTotal()) + "</td>");
            out.println("<td>$" + String.format("%.2f", p.getIva()) + "</td>");
            out.println("<td>$" + String.format("%.2f", p.getTotalConIva()) + "</td>");
            out.println("</tr>");
            totalGeneral += p.getValorTotal();
            totalIva += p.getIva();
            totalConIvaGeneral += p.getTotalConIva();
        }

        out.println("</table>");
        out.println("<p>Total sin IVA: $" + String.format("%.2f", totalGeneral) + "</p>");
        out.println("<p>Total IVA: $" + String.format("%.2f", totalIva) + "</p>");
        out.println("<p>Total con IVA: $" + String.format("%.2f", totalConIvaGeneral) + "</p>");
        out.println("</div>");
        out.println("<form action=\"index.html\" method=\"get\">");
        out.println("<button type=\"submit\">Agregar otro producto</button>");
        out.println("</form>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}

class Producto {
    private String nombre;
    private double valorUnitario;
    private int cantidad;
    private double valorTotal;
    private double iva;
    private double totalConIva;

    public Producto(String nombre, double valorUnitario, int cantidad, double valorTotal, double iva, double totalConIva) {
        this.nombre = nombre;
        this.valorUnitario = valorUnitario;
        this.cantidad = cantidad;
        this.valorTotal = valorTotal;
        this.iva = iva;
        this.totalConIva = totalConIva;
    }

    public String getNombre() {
        return nombre;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public double getIva() {
        return iva;
    }

    public double getTotalConIva() {
        return totalConIva;
    }
}
