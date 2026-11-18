package ui;

import modelos.DisponibilidadTecnico.FranjaHoraria;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

/** Selector de día y horas (de 6:00 a 21:00, cada media hora) que crea una FranjaHoraria. RF-03 y RF-13. */
public class SelectorFranja extends JPanel {

    private final JComboBox<String> dia;
    private final JComboBox<LocalTime> inicio;
    private final JComboBox<LocalTime> fin;

    public SelectorFranja(int horaInicio, int horaFin) {
        super(new GridLayout(1, 3, 10, 0));
        setOpaque(false);
        String[] dias = new String[7];
        for (int i = 0; i < 7; i++) { dias[i] = Tema.nombreDia(DayOfWeek.of(i + 1)); }
        LocalTime[] horas = new LocalTime[31];
        for (int i = 0; i < horas.length; i++) { horas[i] = LocalTime.of(6, 0).plusMinutes(30L * i); }
        dia = Componentes.combo(dias);
        inicio = Componentes.combo(horas);
        fin = Componentes.combo(horas);
        inicio.setSelectedItem(LocalTime.of(horaInicio, 0));
        fin.setSelectedItem(LocalTime.of(horaFin, 0));
        add(columna("Día", dia));
        add(columna("Desde", inicio));
        add(columna("Hasta", fin));
    }

    private JPanel columna(String titulo, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = Componentes.texto(titulo);
        l.setFont(Tema.normal(12));
        p.add(l, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }

    /** Crea la franja; FranjaHoraria valida que la hora de inicio sea antes que la de fin. */
    public FranjaHoraria getFranja() {
        return new FranjaHoraria(DayOfWeek.of(dia.getSelectedIndex() + 1),
                (LocalTime) inicio.getSelectedItem(), (LocalTime) fin.getSelectedItem());
    }
}
