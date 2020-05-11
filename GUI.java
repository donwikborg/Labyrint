import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.paint.Color;


public class GUI extends Application {
  Text statusinfo;
  GridPane rutenett;
  Labyrint lab;
  Knapp[][] brett;
  int indeks = 0;

  class Knapp extends Button {
    char merke = ' ';
    int x, y;

    Knapp(int x, int y) {
      super(" ");
      this.x = x;
      this.y = y;
      setFont(new Font(5));
      setPrefSize(30, 20);
    }
    void settMerke(char c) {
      setText("" + c);
      merke = c;
    }
  }

  class KlikkPaaRuteBehandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e) {
      finnVei((Knapp)e.getSource());
    }
  }

  class ToggleUtveier implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e) {
      finnNyVei();
    }
  }

  class Stoppbehandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent e) {
      Platform.exit();
    }
  }
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage teater) {
    Button velgfilknapp = new Button("Velg fil");
    velgfilknapp.setLayoutX(10); velgfilknapp.setLayoutY(10);
    velgfilknapp.setOnAction(e -> {
      FileChooser filvelger = new FileChooser();
      filvelger.setTitle("Velg labyrintfil");
      File fil = filvelger.showOpenDialog(teater);
      if (fil != null) {
        lab = null;
        try {
          lab = Labyrint.lesFraFil(fil);
        } catch (FileNotFoundException ex) {
          System.exit(1);
        }
        if (lab != null) {
          statusinfo = new Text("~~Trykk paa en rute for aa se et lite øyeblikk med magi~~");
          statusinfo.setFont(new Font(15));
          statusinfo.setX(75); statusinfo.setY(30);

          Button flereutveierknapp = new Button("Toggle utveier");
          flereutveierknapp.setLayoutX(440); flereutveierknapp.setLayoutY(10);
          ToggleUtveier toggle = new ToggleUtveier();
          flereutveierknapp.setOnAction(toggle);

          Button stoppknapp = new Button("Avslutt");
          stoppknapp.setLayoutX(550); stoppknapp.setLayoutY(10);
          Stoppbehandler stopp = new Stoppbehandler();
          stoppknapp.setOnAction(stopp);

          brett = new Knapp[lab.rader][lab.kolonner];

          KlikkPaaRuteBehandler klikk = new KlikkPaaRuteBehandler();
          for (int x = 0; x < lab.rader; x++) {
            for (int y = 0; y < lab.kolonner; y++) {
              brett[x][y] = new Knapp(x, y);
              //brett[x][y].setText("(" + y + ", " + x + ")");
              if (lab.labyrinten[x][y] instanceof HvitRute) {
                brett[x][y].setStyle("-fx-background-color: #FFFFFF");
              }
              if (lab.labyrinten[x][y] instanceof SortRute) {
                brett[x][y].setStyle("-fx-background-color: #000000");
              }
              brett[x][y].setOnAction(klikk);
            }
          }

          GridPane rutenett = new GridPane();
          rutenett.setGridLinesVisible(false);
          for (int x = 0; x < lab.rader; x++) {
            for (int y = 0; y < lab.kolonner; y++) {
              rutenett.add(brett[x][y], x, y);
            }
          }
          rutenett.setLayoutX(10); rutenett.setLayoutY(50);

          Pane kulisser = new Pane();
          kulisser.setPrefSize(1280, 900);
          kulisser.getChildren().add(rutenett);
          kulisser.getChildren().add(statusinfo);
          kulisser.getChildren().add(flereutveierknapp);
          kulisser.getChildren().add(stoppknapp);
          kulisser.getChildren().add(velgfilknapp);

          Scene scene = new Scene(kulisser);

          teater.setTitle("The Ultimate Labyrintloeser");
          teater.setScene(scene);
          teater.show();
        }
      }
    });
    Pane startkulisser = new Pane();
    startkulisser.setPrefSize(250, 100);
    startkulisser.getChildren().add(velgfilknapp);

    Scene startscene = new Scene(startkulisser);

    teater.setTitle("The Ultimate Labyrintloeser");
    teater.setScene(startscene);
    teater.show();
  }

  void resettGrid(Labyrint lab) {
    for (int x = 0; x < lab.rader; x++) {
      for (int y = 0; y < lab.kolonner; y++) {
        if (lab.labyrinten[x][y] instanceof HvitRute) {
          brett[x][y].setStyle("-fx-background-color: #FFFFFF");
        }
        if (lab.labyrinten[x][y] instanceof SortRute) {
          brett[x][y].setStyle("-fx-background-color: #000000");
        }
      }
    }
  }

  static boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
    boolean[][] losning = new boolean[hoyde][bredde];
    java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
    java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
    while (m.find()) {
      int x = Integer.parseInt(m.group(1));
      int y = Integer.parseInt(m.group(2));
      losning[y][x] = true;
    }
    return losning;
  }

  void finnVei(Knapp k) {
    resettGrid(lab);
    lab.finnUtveiFra(k.y, k.x);
    int antallUtveier = lab.utveiliste.stoerrelse();
    if (antallUtveier > 0) {
      String losning = lab.utveiliste.hent(0); //henter den foerste loesningen
      boolean[][] nyttBrett = losningStringTilTabell(losning, lab.kolonner, lab.rader);
      for (int x = 0; x < lab.rader; x++) {
        for (int y = 0; y < lab.kolonner; y++) {
          if (nyttBrett[x][y]) {
            brett[x][y].setStyle("-fx-background-color: #008000");
          }
        }
      }
    }
  }

  void finnNyVei() {
    int antallUtveier = lab.utveiliste.stoerrelse();
    if (antallUtveier > 0) {
      resettGrid(lab);
      indeks++;
      if (indeks >= antallUtveier) {
        indeks = 0;
      }
      String losning = lab.utveiliste.hent(indeks);
      boolean[][] nyttBrett = losningStringTilTabell(losning, lab.kolonner, lab.rader);
      for (int x = 0; x < lab.rader; x++) {
        for (int y = 0; y < lab.kolonner; y++) {
          if (nyttBrett[x][y]) {
            brett[x][y].setStyle("-fx-background-color: #008000");
          }
        }
      }
    }
  }
}
