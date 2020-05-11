class Aapning extends HvitRute {
  public Aapning(int x, int y) {
    super(x, y);
  }
  @Override
  public char tilTegn() {
    return '.';
  }
  @Override
  public void gaa(String utvei) {
    if (this.erBesoekt) {
      return;
    }
    utvei = utvei + this.toString();
    minLab.utveiliste.leggTil(utvei);
    return;
  }
}
