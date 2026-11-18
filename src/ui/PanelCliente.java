package ui;

import enums.EstadoOrden;
import modelos.Cliente;
import modelos.Efectivo;
import modelos.NotificacionCorreo;
import modelos.NotificacionWhatsApp;
import modelos.OrdenServicio;
import modelos.Pago;
import modelos.Servicio;
import modelos.Transferencia;
import servicios.ClienteServicio;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel del CLIENTE: inicio, solicitar servicio, mis órdenes y mi perfil.
 * RF-02, RF-03, RF-08, RF-10, RF-11, RF-12, RF-17, RF-18, RF-19 y RNF-02 (solo ve sus órdenes).
 */
public class PanelCliente extends PanelRol {

    private final Cliente cliente;
    private final ClienteServicio servicio;

    public PanelCliente(VentanaPrincipal ventana, Cliente cliente) {
        super(ventana, cliente);
        this.cliente = cliente;
        this.servicio = ventana.getClienteServicio();
        iniciar();
    }

    @Override
    protected void registrarSecciones() {
        agregarSeccion("Inicio", "Resumen de sus servicios", c -> Iconos.casa(18, c), this::inicio);
        agregarSeccion("Solicitar servicio", "Elija el tipo de trabajo que necesita en su hogar (RF-03)", c -> Iconos.herramienta(18, c), this::solicitar);
        agregarSeccion("Mis órdenes", "Historial, cancelaciones, calificaciones, garantías y pagos (RF-08)", c -> Iconos.lista(18, c), this::misOrdenes);
        agregarSeccion("Mi perfil", "Datos de contacto y canal de avisos (RF-11, RF-19)", c -> Iconos.usuario(18, c), this::perfil);
    }

    @Override
    protected String descripcionUsuario() { return "Cliente · " + cliente.getUbicacion(); }

    @Override
    protected Color colorRol() { return Tema.NARANJA; }

    // ─── Inicio ───────────────────────────────────────────────────────────────

    private JComponent inicio() {
        List<OrdenServicio> ordenes = servicio.consultarHistorial(cliente);
        int activas = 0, finalizadas = 0;
        double saldo = 0;
        for (OrdenServicio o : ordenes) {
            if (o.estaActiva()) { activas++; }
            if (o.getEstado() == EstadoOrden.FINALIZADA) { finalizadas++; }
            if (o.getEstado() != EstadoOrden.CANCELADA && o.getCostoFinal() > 0) { saldo += o.getSaldoPendiente(); }
        }
        JComponent bienvenida = new BannerBienvenida("Hola, " + cliente.getNombre().split(" ")[0],
                "¿Algo en casa necesita arreglo? Un técnico verificado de " + cliente.getUbicacion() + " puede ayudarle.",
                "Solicitar un servicio", () -> irA("Solicitar servicio"));

        JComponent estadisticas = filaEstadisticas(
                Componentes.estadistica("Órdenes activas", String.valueOf(activas), Iconos.reloj(24, Tema.AZUL_INFO), Tema.AZUL_INFO),
                Componentes.estadistica("Servicios terminados", String.valueOf(finalizadas), Iconos.check(24, Tema.VERDE), Tema.VERDE),
                Componentes.estadistica("Saldo por pagar", Tema.dinero(saldo), Iconos.dinero(24, Tema.NARANJA), Tema.NARANJA),
                Componentes.estadistica("Avisos por", cliente.getCanalNotificacion().getNombreCanal(),
                        Iconos.campana(24, Tema.VIOLETA), Tema.VIOLETA));

        List<OrdenServicio> recientes = new ArrayList<>(ordenes);
        java.util.Collections.reverse(recientes);
        if (recientes.size() > 5) { recientes = recientes.subList(0, 5); }
        JComponent lista;
        if (recientes.isEmpty()) {
            lista = Componentes.vacio("Aún no ha solicitado servicios.", Iconos.herramienta(32, Tema.TEXTO_SUAVE));
        } else {
            lista = new TablaOrdenes(recientes, false, this::verDetalle);
        }
        Boton verTodas = new Boton("Ver todas", Boton.Tipo.SECUNDARIO);
        verTodas.addActionListener(e -> irA("Mis órdenes"));
        return pila(bienvenida, estadisticas,
                tarjeta("Órdenes recientes", "Doble clic para ver el detalle", lista, verTodas));
    }

    // ─── Solicitar servicio (RF-03) ───────────────────────────────────────────

    private JComponent solicitar() {
        List<Servicio> servicios = servicio.getServiciosDisponibles();
        JPanel rejilla = new JPanel(new GridLayout(0, 3, 16, 16));
        rejilla.setOpaque(false);
        for (Servicio s : servicios) { rejilla.add(tarjetaServicio(s)); }
        JComponent nota = new PanelTarjeta(new BorderLayout()).conFondo(Tema.suave(Tema.AZUL_INFO, 0.07f), Tema.suave(Tema.AZUL_INFO, 0.25f));
        JLabel texto = new JLabel("<html><b>¿Cómo funciona?</b><br>1. Elija el servicio y describa el problema.<br>"
                + "2. Un administrador asigna un técnico de su zona y le enviamos el costo estimado.<br>"
                + "3. El técnico hace el trabajo; usted paga y califica.</html>", Iconos.campana(20, Tema.AZUL_INFO), SwingConstants.LEFT);
        texto.setIconTextGap(12);
        texto.setFont(Tema.normal(13.5f));
        texto.setForeground(new Color(0x1E, 0x40, 0xAF));
        nota.add(texto);
        return pila(nota, rejilla);
    }

    private JComponent tarjetaServicio(Servicio s) {
        Color color = Iconos.colorServicio(s.getTipo());
        PanelTarjeta t = new PanelTarjeta();
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        JComponent icono = Componentes.cuadroIcono(Iconos.servicio(s.getTipo(), 30, color), color, 58);
        icono.setAlignmentX(LEFT_ALIGNMENT);
        t.add(icono);
        t.add(Box.createVerticalStrut(14));
        JLabel nombre = Componentes.titulo(s.getTipo(), 18);
        nombre.setAlignmentX(LEFT_ALIGNMENT);
        t.add(nombre);
        t.add(Box.createVerticalStrut(8));
        for (JLabel l : new JLabel[]{
                linea(Iconos.reloj(15, Tema.TEXTO_SUAVE), "Duración estimada: " + formatoHoras(s.getDuracionEstimadaHoras())),
                linea(Iconos.escudo(15, Tema.TEXTO_SUAVE), "Garantía de " + s.getDiasGarantia() + " días"),
                linea(Iconos.dinero(15, Tema.TEXTO_SUAVE), s.getFactorTarifa() == 1 ? "Tarifa normal" : "Tarifa × " + s.getFactorTarifa())}) {
            t.add(l);
            t.add(Box.createVerticalStrut(4));
        }
        t.add(Box.createVerticalStrut(14));
        Boton pedir = new Boton("Solicitar", Iconos.mas(14, Color.WHITE), Boton.Tipo.PRIMARIO);
        pedir.setAlignmentX(LEFT_ALIGNMENT);
        pedir.addActionListener(e -> formularioSolicitud(s));
        t.add(pedir);
        return t;
    }

    private static JLabel linea(Icon icono, String texto) {
        JLabel l = Componentes.texto(texto);
        l.setIcon(icono);
        l.setIconTextGap(8);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private static String formatoHoras(double horas) {
        return (horas == Math.floor(horas) ? String.valueOf((int) horas) : String.valueOf(horas)) + " h";
    }

    private void formularioSolicitud(Servicio elegido) {
        JComboBox<String> tipo = Componentes.combo(nombresServicios());
        tipo.setSelectedItem(elegido.getTipo());
        JTextField ubicacion = Componentes.campoTexto(cliente.getUbicacion());
        JTextArea descripcion = Componentes.areaTexto(3);
        SelectorFranja horario = new SelectorFranja(8, 10);
        Formulario f = new Formulario(this, "Solicitar " + elegido.getTipo(), "RF-03 · La solicitud queda PENDIENTE hasta que se asigne un técnico",
                Iconos.servicio(elegido.getTipo(), 22, Color.WHITE), "Enviar solicitud");
        f.campo("Tipo de servicio", tipo)
                .campo("Dirección / municipio de la visita", ubicacion)
                .campo("Describa el problema", Componentes.conBorde(descripcion))
                .campo("¿Cuándo puede recibir al técnico?", horario)
                .nota("Solo se asignan técnicos de " + "la especialidad y zona indicadas, libres en ese horario (RF-04). "
                        + "Le avisaremos por " + cliente.getCanalNotificacion().getNombreCanal() + " cada cambio (RF-19).", Tema.AZUL_INFO);
        f.mostrar(() -> Acciones.ejecutar(f, "¡Solicitud enviada!", () -> {
            OrdenServicio orden = servicio.solicitarServicio(cliente, (String) tipo.getSelectedItem(),
                    ubicacion.getText(), descripcion.getText(), horario.getFranja());
            System.out.println("Su número de orden es " + orden.getCodigo() + ". Puede seguirla en \"Mis órdenes\".");
            return true;
        }));
        refrescar();
    }

    private String[] nombresServicios() {
        List<Servicio> lista = servicio.getServiciosDisponibles();
        String[] nombres = new String[lista.size()];
        for (int i = 0; i < nombres.length; i++) { nombres[i] = lista.get(i).getTipo(); }
        return nombres;
    }

    // ─── Mis órdenes (RF-08) ──────────────────────────────────────────────────

    private JComponent misOrdenes() {
        List<OrdenServicio> ordenes = servicio.consultarHistorial(cliente);
        if (ordenes.isEmpty()) {
            return tarjeta("Mis órdenes", null, Componentes.vacio("Aún no tiene órdenes. ¡Solicite su primer servicio!",
                    Iconos.lista(32, Tema.TEXTO_SUAVE)));
        }
        TablaOrdenes tabla = new TablaOrdenes(ordenes, false, this::verDetalle);
        Boton detalle = new Boton("Ver detalle", Iconos.ojo(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        Boton cancelar = new Boton("Cancelar", Iconos.equis(14, Tema.ROJO), Boton.Tipo.PELIGRO);
        Boton calificar = new Boton("Calificar", Iconos.estrella(15, Tema.TEXTO, false), Boton.Tipo.SECUNDARIO);
        Boton garantia = new Boton("Garantía", Iconos.escudo(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        Boton pagar = new Boton("Pagar", Iconos.dinero(15, Color.WHITE), Boton.Tipo.EXITO);

        detalle.addActionListener(e -> verDetalle(seleccionada(tabla)));
        cancelar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { cancelar(o); } });
        calificar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { calificar(o); } });
        garantia.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { revisionGarantia(o); } });
        pagar.addActionListener(e -> { OrdenServicio o = seleccionada(tabla); if (o != null) { pagar(o); } });

        // Solo se habilitan las acciones que tienen sentido para el estado de la orden elegida.
        Runnable actualizar = () -> {
            OrdenServicio o = tabla.getSeleccionada();
            detalle.setEnabled(o != null);
            cancelar.setEnabled(o != null && o.getEstado() == EstadoOrden.PENDIENTE);
            calificar.setEnabled(o != null && o.getEstado() == EstadoOrden.FINALIZADA && o.getCalificacion() == null);
            garantia.setEnabled(o != null && o.getGarantia() != null && o.getGarantia().estaVigente(LocalDate.now()));
            pagar.setEnabled(o != null && o.getEstado() != EstadoOrden.CANCELADA && o.getSaldoPendiente() > 0);
        };
        tabla.alCambiarSeleccion(actualizar);
        actualizar.run();
        tabla.seleccionar(0);

        JPanel acciones = Componentes.fila(8, detalle, cancelar, calificar, garantia, pagar);
        acciones.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.add(tabla, BorderLayout.CENTER);
        cuerpo.add(acciones, BorderLayout.SOUTH);
        return tarjeta("Historial de órdenes", ordenes.size() + " orden(es) · seleccione una para ver las acciones disponibles", cuerpo);
    }

    // RF-12
    private void cancelar(OrdenServicio o) {
        JTextArea motivo = Componentes.areaTexto(3);
        Formulario f = new Formulario(this, "Cancelar " + o.getCodigo(), "RF-12 · Solo mientras no tenga técnico asignado",
                Iconos.equis(20, Color.WHITE), "Cancelar solicitud");
        f.campo("¿Por qué cancela la solicitud?", Componentes.conBorde(motivo));
        f.mostrar(() -> Acciones.ejecutar(f, "Solicitud cancelada", () -> servicio.cancelarSolicitud(cliente, o.getCodigo(), motivo.getText())));
        refrescar();
    }

    // RF-10
    private void calificar(OrdenServicio o) {
        SelectorEstrellas estrellas = new SelectorEstrellas(0, true, 34);
        JTextArea comentario = Componentes.areaTexto(3);
        String tecnico = o.getTecnico() == null ? "el técnico" : o.getTecnico().getNombre();
        Formulario f = new Formulario(this, "Calificar servicio", "RF-10 · ¿Cómo le fue con " + tecnico + "?",
                Iconos.estrella(22, Color.WHITE, true), "Enviar calificación");
        f.campo("Calificación", estrellas).campo("Comentario (opcional)", Componentes.conBorde(comentario));
        f.mostrar(() -> Acciones.ejecutar(f, "¡Gracias por calificar!", () -> {
            servicio.calificarServicio(cliente, o.getCodigo(), estrellas.getValor(), comentario.getText());
            return true;
        }));
        refrescar();
    }

    // RF-18
    private void revisionGarantia(OrdenServicio o) {
        JTextArea problema = Componentes.areaTexto(3);
        Formulario f = new Formulario(this, "Solicitar revisión por garantía", "RF-18 · Orden " + o.getCodigo(),
                Iconos.escudo(22, Color.WHITE), "Solicitar revisión");
        f.nota("Garantía vigente hasta el " + o.getGarantia().getFechaFin() + ". Cubre: " + o.getGarantia().getCobertura() + ".", Tema.VERDE)
                .campo("¿Qué problema se presentó?", Componentes.conBorde(problema));
        f.mostrar(() -> Acciones.ejecutar(f, "Revisión registrada", () -> {
            servicio.solicitarRevisionGarantia(cliente, o.getCodigo(), LocalDate.now(), problema.getText());
            return true;
        }));
        refrescar();
    }

    // RF-17 y RNF-05
    private void pagar(OrdenServicio o) {
        JTextField monto = Componentes.campoTexto(String.valueOf((long) o.getSaldoPendiente()));
        JComboBox<String> metodo = Componentes.combo(new String[]{"Transferencia", "PanelTarjeta (datáfono)", "Efectivo"});
        JTextField referencia = Componentes.campoTexto("");
        metodo.addActionListener(e -> referencia.setEnabled(metodo.getSelectedIndex() != 2));
        Formulario f = new Formulario(this, "Registrar pago", "RF-17 · Orden " + o.getCodigo() + " · Saldo pendiente "
                + Tema.dinero(o.getSaldoPendiente()), Iconos.dinero(22, Color.WHITE), "Registrar pago");
        f.campo("Valor a pagar", monto)
                .campo("Método de pago", metodo)
                .campo("Referencia (número de transferencia o autorización del datáfono)", referencia)
                .nota("Por su seguridad NUNCA escriba el número de su tarjeta (RNF-05). "
                        + "El pago queda pendiente hasta que el administrador lo confirme.", Tema.AMBAR);
        f.mostrar(() -> Acciones.ejecutar(f, "Pago registrado", () -> {
            double valor;
            try {
                valor = Double.parseDouble(monto.getText().trim().replace(".", "").replace(",", "."));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Escriba el valor solo con números, por ejemplo 150000.");
            }
            Pago pago;   // POLIMORFISMO: una variable Pago, tres tipos posibles
            switch (metodo.getSelectedIndex()) {
                case 0: pago = new Transferencia(valor, LocalDate.now(), referencia.getText()); break;
                case 1: pago = new modelos.Tarjeta(valor, LocalDate.now(), referencia.getText()); break;
                default: pago = new Efectivo(valor, LocalDate.now());
            }
            servicio.registrarPago(cliente, o.getCodigo(), pago);
            System.out.println("Quedará CONFIRMADO cuando el administrador lo revise.");
            return true;
        }));
        refrescar();
    }

    // ─── Mi perfil (RF-11, RF-19) ─────────────────────────────────────────────

    private JComponent perfil() {
        JTextField nombre = Componentes.campoTexto(cliente.getNombre());
        JTextField telefono = Componentes.campoTexto(cliente.getTelefono());
        JTextField correo = Componentes.campoTexto(cliente.getCorreo());
        JTextField ubicacion = Componentes.campoTexto(cliente.getUbicacion());
        JPanel campos = new JPanel(new GridLayout(0, 2, 18, 10));
        campos.setOpaque(false);
        campos.add(conEtiqueta("Nombre completo", nombre));
        campos.add(conEtiqueta("Documento (no se puede cambiar)", deshabilitado(cliente.getDocumento())));
        campos.add(conEtiqueta("Celular", telefono));
        campos.add(conEtiqueta("Correo electrónico", correo));
        campos.add(conEtiqueta("Ubicación", ubicacion));
        Boton guardar = new Boton("Guardar cambios", Iconos.check(15, Color.WHITE), Boton.Tipo.PRIMARIO);
        guardar.addActionListener(e -> {
            Acciones.ejecutar(this, "Datos actualizados", () -> {
                servicio.actualizarDatos(cliente, nombre.getText(), telefono.getText(), correo.getText(), ubicacion.getText());
                return true;
            });
            refrescar();
        });
        JPanel datos = new JPanel(new BorderLayout(0, 16));
        datos.setOpaque(false);
        datos.add(campos, BorderLayout.CENTER);
        datos.add(Componentes.fila(0, guardar), BorderLayout.SOUTH);

        // Canal de avisos
        boolean whatsapp = cliente.getCanalNotificacion() instanceof NotificacionWhatsApp;
        JPanel canales = new JPanel(new GridLayout(1, 2, 16, 0));
        canales.setOpaque(false);
        canales.add(opcionCanal("WhatsApp", "Al celular " + cliente.getTelefono(), Iconos.telefono(26, Tema.VERDE), Tema.VERDE, whatsapp,
                () -> cambiarCanal(new NotificacionWhatsApp())));
        canales.add(opcionCanal("Correo", "A " + cliente.getCorreo(), Iconos.correo(26, Tema.AZUL_INFO), Tema.AZUL_INFO, !whatsapp,
                () -> cambiarCanal(new NotificacionCorreo())));

        // Contraseña
        JPasswordField actual = Componentes.campoClave();
        JPasswordField nueva = Componentes.campoClave();
        Boton cambiar = new Boton("Cambiar contraseña", Iconos.candado(15, Tema.TEXTO), Boton.Tipo.SECUNDARIO);
        cambiar.addActionListener(e -> {
            if (Acciones.ejecutar(this, "Contraseña actualizada", () -> {
                servicio.cambiarContrasenia(cliente, new String(actual.getPassword()), new String(nueva.getPassword()));
                return true;
            })) { actual.setText(""); nueva.setText(""); }
        });
        JPanel clave = new JPanel(new GridLayout(1, 3, 18, 0));
        clave.setOpaque(false);
        clave.add(conEtiqueta("Contraseña actual", actual));
        clave.add(conEtiqueta("Contraseña nueva", nueva));
        JPanel boton = new JPanel(new BorderLayout());
        boton.setOpaque(false);
        boton.add(cambiar, BorderLayout.SOUTH);
        clave.add(boton);

        return pila(tarjeta("Datos de contacto", "RF-11 · Se validan todos antes de guardar", datos),
                tarjeta("¿Por dónde quiere recibir los avisos?", "RF-19 · Le avisamos cada vez que su orden cambia de estado", canales),
                tarjeta("Seguridad", "RNF-05 · La contraseña se guarda cifrada", clave));
    }

    private void cambiarCanal(interfaces.Notificable canal) {
        Acciones.ejecutar(this, "Canal actualizado", () -> { servicio.cambiarCanalNotificacion(cliente, canal); return true; });
        refrescar();
    }

    private static JComponent opcionCanal(String titulo, String detalle, Icon icono, Color color, boolean activo, Runnable alElegir) {
        PanelTarjeta t = new PanelTarjeta(new BorderLayout(14, 0));
        if (activo) { t.conFondo(Tema.suave(color, 0.08f), color); }
        t.add(Componentes.cuadroIcono(icono, color, 50), BorderLayout.WEST);
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(Componentes.titulo(titulo, 16));
        textos.add(Componentes.texto(detalle));
        t.add(textos, BorderLayout.CENTER);
        if (activo) {
            JPanel p = new JPanel(new GridBagLayout());
            p.setOpaque(false);
            p.add(new Insignia("En uso", color));
            t.add(p, BorderLayout.EAST);
        } else {
            Boton usar = new Boton("Usar este", Boton.Tipo.SECUNDARIO);
            usar.addActionListener(e -> alElegir.run());
            JPanel p = new JPanel(new GridBagLayout());
            p.setOpaque(false);
            p.add(usar);
            t.add(p, BorderLayout.EAST);
        }
        return t;
    }

    static JPanel conEtiqueta(String etiqueta, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        p.add(Componentes.etiquetaCampo(etiqueta), BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    static JTextField deshabilitado(String valor) {
        JTextField campo = Componentes.campoTexto(valor);
        campo.setEditable(false);
        campo.setBackground(new Color(0xF3, 0xF4, 0xF6));
        return campo;
    }

    // ─── Banner de bienvenida (degradado azul con botón naranja) ──────────────

    static class BannerBienvenida extends JPanel {
        BannerBienvenida(String titulo, String texto, String textoBoton, Runnable accion) {
            super(new BorderLayout(20, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(26, 30, 26, 30));
            JPanel textos = new JPanel(new GridLayout(2, 1, 0, 6));
            textos.setOpaque(false);
            JLabel t = new JLabel(titulo);
            t.setFont(Tema.negrita(26));
            t.setForeground(Color.WHITE);
            JLabel d = new JLabel(texto);
            d.setFont(Tema.normal(15));
            d.setForeground(new Color(0xDB, 0xE5, 0xF0));
            textos.add(t);
            textos.add(d);
            add(textos, BorderLayout.CENTER);
            if (textoBoton != null) {
                Boton b = new Boton(textoBoton, Iconos.mas(15, Color.WHITE), Boton.Tipo.PRIMARIO);
                b.addActionListener(e -> accion.run());
                JPanel p = new JPanel(new GridBagLayout());
                p.setOpaque(false);
                p.add(b);
                add(p, BorderLayout.EAST);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suavizar(g);
            g2.setPaint(new GradientPaint(0, 0, Tema.AZUL, getWidth(), getHeight(), Tema.AZUL_CLARO));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            // casa y herramienta decorativas al fondo
            Icon deco = Iconos.casa(130, new Color(255, 255, 255, 22));
            deco.paintIcon(this, g2, getWidth() - 360, -12);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
