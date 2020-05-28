


public interface SubFixer {

	/**
	 * Detekuje vsetky casove stopy vhodneho formatu v riadku a nahradi ich casmi
	 * posunute o by.
	 * 
	 * @param line riadok v ktorom sa to ma opravit
	 * @param by   o kolko sekund, stotin
	 * @return Priklad:
	 * 
	 *         <pre>
	 * 
	 * fixline("00:20:41,150 --> 00:20:45,109",2.6f) vrati "00:20:43,750 --> 00:20:47,709"
	 * fixline("00:20:41.150 --> 00:20:45.109",2.6f) vrati "00:20:43.750 --> 00:20:47.709"
	 * fixline("- Meet me at 4:32?",31.4f) vrati "- Meet me at 4:32?"
	 * 
	 *         </pre>
	 */
	public String fixline(String line, float by);

	/**
	 * Podla hodnot v fps otvori subor a posunie casy. Na kazdy riadok aplikuje
	 * metodu fixline a vysledok zapisuje do noveho suboru. V pameti sa nikdy
	 * neudrzuje cely subor - kazdy riadok sa po precitani upravi a rovno zapise do
	 * noveho suboru. Podporovane formaty tituliek: srt a vtt
	 * https://en.wikipedia.org/wiki/WebVTT
	 * https://en.wikipedia.org/wiki/SubRip
	 * @throws InterruptedException 
	 */
	public void fix(FixParams fps) throws InterruptedException;
}
