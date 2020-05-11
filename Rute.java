abstract class Rute {
  private int x;
  private int y;
  protected Rute naboNord, naboOest, naboSoer, naboVest;
  protected Rute[] naboer;
  protected Labyrint minLab;
  protected boolean erBesoekt = false;
  protected static Lenkeliste<String> liste = new Lenkeliste<String>();

  protected Rute(int rad, int kolonne) {
    x = rad;
    y = kolonne;
  }

  public abstract char tilTegn();
  public abstract boolean erAapning();

  public void settNaboer(Labyrint labyrint, int maxrader, int maxkoler) { //denne kunne nok ganske aapenbart ha blitt skrevet kortere.
    minLab = labyrint;
    int maxrad = maxrader - 1; //pga indeks
    int maxkol = maxkoler - 1; //pga indeks
    if (x == 0 && y == 0) {
      naboNord = null;
      naboVest = null;
      naboSoer = minLab.labyrinten[1][0];
      naboOest = minLab.labyrinten[0][1];
    }
    else if (x == 0 && y == maxkol) {
      naboNord = null;
      naboOest = null;
      naboSoer = minLab.labyrinten[1][maxkol];
      naboVest = minLab.labyrinten[0][maxkol-1];
    }
    else if (x == maxrad && y == 0) {
      naboSoer = null;
      naboVest = null;
      naboNord = minLab.labyrinten[maxrad-1][0];
      naboOest = minLab.labyrinten[maxrad][1];
    }
    else if (x == maxrad && y == maxkol) {
      naboSoer = null;
      naboOest = null;
      naboNord = minLab.labyrinten[maxrad-1][maxkol];
      naboVest = minLab.labyrinten[maxrad][maxkol-1];
    }
    else if (x == 0) {
      naboNord = null;
      naboSoer = minLab.labyrinten[1][y];
      naboOest = minLab.labyrinten[0][y+1];
      naboVest = minLab.labyrinten[x][y-1];
    }
    else if (x == maxrad) {
      naboSoer = null;
      naboNord = minLab.labyrinten[x-1][y];
      naboOest = minLab.labyrinten[x][y+1];
      naboVest = minLab.labyrinten[x][y-1];
    }
    else if (y == 0) {
      naboVest = null;
      naboNord = minLab.labyrinten[x-1][y];
      naboSoer = minLab.labyrinten[x+1][y];
      naboOest = minLab.labyrinten[x][y+1];
    }
    else if (y == maxkol) {
      naboOest = null;
      naboNord = minLab.labyrinten[x-1][y];
      naboSoer = minLab.labyrinten[x+1][y];
      naboVest = minLab.labyrinten[x][y-1];
    }
    else {
      naboNord = minLab.labyrinten[x-1][y];
      naboSoer = minLab.labyrinten[x+1][y];
      naboOest = minLab.labyrinten[x][y+1];
      naboVest = minLab.labyrinten[x][y-1];
    }
    naboer = new Rute[] {naboNord, naboOest, naboSoer, naboVest};
  }
  @Override
  public String toString() {
    return "(" + y + ", " + x + ")"; //fordi man av en eller annen merkelig grunn vil skrive ut koordinatene som (kolonne, rad) istedenfor (rad, kolonne)
  }
  public void settBesoektTilFalse() {
    erBesoekt = false;
  }
  abstract void gaa(String utvei);

  /*
  private void gaa(Rute rute, String utvei) {
    if (rute.erBesoekt) { //hvis ruten er besoekt, maa vi proeve en annen. denne sjekken er viktig, og gjoer at vi ikke havner paa samme rute flere ganger.
      return;
    }
    if (rute.tilTegn() == '#') { //svarte ruter duger ikke. da stopper vi opp
      return;
    }
    if (rute.erAapning()) { //basissteget i rekursjonen. her stopper den opp
      utvei = utvei + rute.toString();
      liste.leggTil(utvei); //legger utveien til i lenkelisten.
      //erBesoekt = false;
      return;
    }
    rute.erBesoekt = true; //setter at ruten er besoekt til true. soerger for at man ikke besoeker samme rute flere ganger.
    utvei = utvei + rute.toString() + " --> ";
    if (rute.tilTegn() == '.') { //trenger vel eeegentlig ikke denne if-sjekken, men for sikkerhets skyld
      for (int i = 0; i < rute.naboer.length; i++) {
        if (rute.naboer[i].tilTegn() == '.') { //hvis naboen er hvit eller aapning, saa gaar vi videre
          gaa(rute.naboer[i], utvei); //rekursjooooon, gaar og gaar helt til vi kommer til en aapning.
        }
      }
    }
  }
  */

  public void finnUtvei() {
    //nullstiller listen for hver gang vi oensker aa finne en utvei.
    //for (String e:liste) {
    //  liste.fjern();
    //}

    //lager ny utveiliste ved hjelp av gaa-metoden
    //gaa(this, "");
    minLab.utveiliste = new Lenkeliste<String>();
    this.gaa("");
    //return liste; //og returnerer den nye listen
  }
}
