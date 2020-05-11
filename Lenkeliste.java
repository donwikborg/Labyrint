import java.util.Iterator;

class Lenkeliste<T> implements Liste<T> {
  protected Node start;
  protected int stoerrelse; // Counter for størrelse på listen.

  public Iterator<T> iterator(){
    return new LenkelisteIterator();
  }
  //For å kunne iterere gjennom lister
  class LenkelisteIterator implements Iterator<T>{
    Node peker = start;
    //sjekker om det finnes elementer videre i listen
    public boolean hasNext() {
      if (peker == null) {
        return false;
      }
      return true;
    }
    //returnerer innhold om hasNext er true, returnerer null om ikke. 
    public T next() {
      if (hasNext()){
        T data = peker.innhold;
        peker = peker.neste;
        return data;
      }
      return null;
    }
  }

  public class Node{
    Node neste; // referanse til neste node, som er tom til å begynne med.
    T innhold; // Innholdet i noden av type T.
    public Node (T innhold){ // Konstruktør som legger innhold i noden.
      this.innhold = innhold;
    }
  }




  @Override
  public int stoerrelse(){
    // Bruker counter for størrelse.
    return stoerrelse;
  }

  @Override
  public void leggTil(int pos, T x){
    // Den nye noden med innholdet som skal legges til.
    Node nyNode = new Node(x);

    // Hvis pos er negativ eller større en størrelsen får vi error.
    // Aksepterer pos = størrelse, da blir tall lagt til i slutten av listen.
    if (pos < 0 || pos > stoerrelse){
      throw new UgyldigListeIndeks(pos);

    // Hvis pos er 0 legges ny node først i listen, og resten av listen
    // blir forskjøvet.
    } else if (pos == 0){
      nyNode.neste = start;
      start = nyNode;
      stoerrelse++;

    } else {
      Node peker = start; // Lager peker.

      // Flytter peker til plassen før pos i listen.
      for(int teller = 0; teller < pos-1; teller++){
        peker = peker.neste; //peker er nå på siste node.
      }

      // Endrer rekkefølge slik at ny node nå er på pos, og noder bak blir
      // forskjøvet. Legge til 1 i counter for størrelse.
      nyNode.neste = peker.neste;
      peker.neste = nyNode;
      stoerrelse++;
    }
  }

  @Override
  public void leggTil(T x){
    Node nyNode = new Node(x);

    // Hvis listen er tom settes start-pekeren til den nye noden.
    if (stoerrelse == 0){
      start = nyNode;
      stoerrelse ++;

    // Flytter peker til nest siste node, og setter inn den nye noden på
    // plassen etter. Legger til 1 til størrelsecounter.
    } else {
    Node peker = start;

    for(int teller = 0; teller < stoerrelse-1 ; teller++){
      peker = peker.neste;
    }
    peker.neste = nyNode;
    stoerrelse ++;
    }

  }

  @Override
  public void sett(int pos, T x){
    // Kaster unntak hvis pos er under null, lik eller større en lengden på
    // listen eller listen er tom.
    if (pos < 0 || stoerrelse == 0 || pos >= stoerrelse ){
      throw new UgyldigListeIndeks(pos);

    // Hvis ikke unntak blir kasts, settes innholdet på index pos til x.
    } else {
      Node peker = start;

      for(int teller = 0; teller < pos; teller++){
        peker = peker.neste;
      }
      peker.innhold = x;
    }
  }

  @Override
  public T hent(int pos){
    // Kaster unntak hvis pos er under null, lik eller større en lengden på
    // listen eller listen er tom.
    if (pos < 0 ||stoerrelse == 0 || pos >= stoerrelse){
      throw new UgyldigListeIndeks(pos);

    // Returnerer innhold fra index pos.
    } else {
      Node peker = start;

      for(int teller = 0; teller < pos; teller++){
        peker = peker.neste;
      }
      return peker.innhold;
    }
  }

  @Override
  public T fjern(int pos){
    // UgyldigListeIndeks(-1) vil bli kastet.
    if (stoerrelse == 0){
      throw new UgyldigListeIndeks(-1);

    // Kaster error dersom pos er utenfor listen.
    } else if (pos < 0 || pos >= stoerrelse){
      throw new UgyldigListeIndeks(pos);

    // Hvis pos == 0 vil element på index 0 bli fjerna, samme som fjern().
    } else if (pos == 0) {
      return this.fjern();

    // Navigerer til element før det som skal bli fjernet. Setter innhold i
    // noden som skal bor til skalUt, "hopper over" noden i listen, så den
    // faller bort, reduserer counter med 1 og returnerer skalUt.
    } else {
      Node peker = start;

      for(int teller = 0; teller < pos-1; teller++){
        peker = peker.neste;
      }
      T skalUt = peker.neste.innhold;
      peker.neste = peker.neste.neste;
      stoerrelse --;

      return skalUt;
    }
  }

  @Override
  public T fjern(){
    // Kaster ugyldig index -1 hvis listen er tom.
    if (stoerrelse == 0){
      throw new UgyldigListeIndeks(-1);

    // Fjerner elementet som ligger først i lista, altså det på index 0.
    } else {
      T skalUt = start.innhold;
      start = start.neste;
      stoerrelse--;
      return skalUt;
    }
  }
}
