import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class Labyrint {
  public int rader;
  public int kolonner;
  protected Rute[][] labyrinten;
  private boolean utfyllende = false;
  protected Lenkeliste<String> utveiliste = new Lenkeliste<String>();


  private Labyrint(int rad, int kol) {
    rader = rad;
    kolonner = kol;
    labyrinten = new Rute[rad][kol];
  }

  public static Labyrint lesFraFil(File fil) throws FileNotFoundException {
      Scanner lesFil = new Scanner(fil);
      int rader = lesFil.nextInt();
      int kolonner = lesFil.nextInt();
      lesFil.nextLine();
      Labyrint laby = new Labyrint(rader, kolonner);

      char hvitrute = '.';
      char sortrute = '#';
      for (int rad = 0; rad < rader; rad++) {
        String linje = lesFil.nextLine();
        //System.out.println(linje);
        for (int kol = 0; kol < kolonner; kol++) {
          if (linje.charAt(kol) == hvitrute) {
            if (kol == 0 || rad == 0 || rad == rader-1 || kol == kolonner-1) {
              laby.labyrinten[rad][kol] = new Aapning(rad,kol);
            } else {
              laby.labyrinten[rad][kol] = new HvitRute(rad,kol); //lager labyrinten
            }
          }
          else if (linje.charAt(kol) == sortrute) {
            laby.labyrinten[rad][kol] = new SortRute(rad,kol); //lager labyrinten
          }
        }
      }
      //gaar gjennom labyrinten og setter naboene
      for (int x = 0; x < rader; x++) {
        for (int y = 0; y < kolonner; y++) {
          laby.labyrinten[x][y].settNaboer(laby, rader, kolonner);
        }
      }
      return laby;
    }
    public void skrivUtfyllende() { //denne har noe aa si for oblig5alt-filen, den som er gjort litt med slik at man kan skrive ut detaljert info
      utfyllende = true;
    }
    public void ingenBesoekt() { //"nullstiller" rutene
      for (int x = 0; x < rader; x++) {
        for (int y = 0; y < kolonner; y++) {
          labyrinten[x][y].settBesoektTilFalse();
        }
      }
    }
    public Lenkeliste<String> finnUtveiFra(int kol, int rad) {
      //Lenkeliste<String> utveiliste = new Lenkeliste<String>();
      try {
        ingenBesoekt();
        labyrinten[rad][kol].finnUtvei();
        if (utfyllende) {
          System.out.println(this);
          System.out.println(kol + " " + rad);
          int teller = 0;
          int kort = 10000; //bare i tilfelle korteste rute er ganske lang
          String korteste = "";
          for (String e:utveiliste) {
            teller++;
            if (e.length() < kort) {
              kort = e.length();
              korteste = e;
            }
          }
          if (teller > 1) {
            System.out.println("Fant " + teller + " utveier.");
            System.out.println("Korteste utvei funnet er: " + korteste);
            System.out.println();
          }
        }

        return utveiliste;
      } catch (ArrayIndexOutOfBoundsException e) { //i tilfelle man skriver inn ugyldige koordinater.
        System.out.println("Du er utenfor labyrinten. Skriv gyldige koordinater.");
      }
      return new Lenkeliste<String>(); //denne har stoerrelse null, saa det blir ikke skrevet ut noe.
    }
    @Override
    public String toString() {
      String utskrift = "";
      for (int x = 0; x < rader; x++) {
        utskrift += "\n";
        for (int y = 0; y < kolonner; y++) {
          utskrift += labyrinten[x][y].tilTegn();
        }
      }
      return utskrift;
    }
  }
