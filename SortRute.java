class SortRute extends Rute {
  public SortRute(int x, int y) {
    super(x, y);
  }
  @Override
  public char tilTegn() {
    return '#';
  }
  @Override
  public boolean erAapning() { //kan ikke vaere aapning.
    return false;
  }
  @Override
  public void gaa(String utvei) {
    return;
  }
}
