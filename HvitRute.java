class HvitRute extends Rute {
  public HvitRute(int x, int y) {
    super(x, y);
  }
  @Override
  public char tilTegn() {
    return '.';
  }
  public boolean erAapning() { //om en hvit rute har minst én naborute som er null, betyr det at den er en aapning.
    if (naboNord != null && naboSoer != null && naboOest != null && naboVest != null) {
      return false;
    }
    return true;
  }
  @Override
  public void gaa(String utvei) {
    if (this.erBesoekt) {
      return;
    }
    this.erBesoekt = true;
    utvei = utvei + this.toString() + " --> ";
    for (int i = 0; i < this.naboer.length; i++) {
        this.naboer[i].gaa(utvei);
      }
    }
  }
