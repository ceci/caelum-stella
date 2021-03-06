package br.com.caelum.stella.boleto.bancos;

import java.net.URL;

import static br.com.caelum.stella.boleto.utils.StellaStringUtils.leftPadWithZeros;
import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Emissor;

public class Itau extends AbstractBanco implements Banco {

	private static final long serialVersionUID = 1L;

	private static final String NUMERO_ITAU = "341";
	private static final String DIGITO_ITAU = "7";

	@Override
	public String geraCodigoDeBarrasPara(Boleto boleto) {
		Emissor emissor = boleto.getEmissor();
		StringBuilder campoLivre = new StringBuilder();
		campoLivre.append(getCarteiraDoEmissorFormatado(emissor));
		campoLivre.append(getNossoNumeroDoEmissorFormatado(emissor));
		campoLivre.append(emissor.getAgenciaFormatado());
		campoLivre.append(getContaCorrenteDoEmissorFormatado(emissor)).append("000");
		campoLivre.insert(20, this.geradorDeDigito
				.geraDigitoMod10(campoLivre.substring(11, 20)));
		campoLivre.insert(11, this.geradorDeDigito.geraDigitoMod10(campoLivre
				.substring(11, 20).concat(campoLivre.substring(0, 11))));
		return new CodigoDeBarrasBuilder(boleto).comCampoLivre(campoLivre);
	}

	@Override
	public String getNumeroFormatadoComDigito() {
		StringBuilder builder = new StringBuilder();
		builder.append(getNumeroFormatado()).append("-");
		return builder.append(DIGITO_ITAU).toString();
	}

	@Override
	public String getCarteiraDoEmissorFormatado(Emissor emissor) {
		return leftPadWithZeros(emissor.getCarteira(), 3);
	}

	@Override
	public String getContaCorrenteDoEmissorFormatado(Emissor emissor) {
		return leftPadWithZeros(emissor.getContaCorrente(), 5);
	}

	@Override
	public URL getImage() {
		String arquivo = "/br/com/caelum/stella/boleto/img/%s.png";
		String imagem = String.format(arquivo, getNumeroFormatado());
		return getClass().getResource(imagem);
	}

	@Override
	public String getNossoNumeroDoEmissorFormatado(Emissor emissor) {
		return leftPadWithZeros(emissor.getNossoNumero(), 8);
	}
	
	@Override
	public String getNossoNumeroECodDocumento(Boleto boleto) {
		String valor = super.getNossoNumeroECodDocumento(boleto);
		Emissor emissor = boleto.getEmissor();
		return valor.concat("-").concat(emissor.getDigitoNossoNumero());
	}

	@Override
	public String getNumeroFormatado() {
		return NUMERO_ITAU;
	}
	
	@Override
	public String getAgenciaECodigoCedente(Emissor emissor) {
		StringBuilder builder = new StringBuilder();
		builder.append(emissor.getAgenciaFormatado()).append("/");
		builder.append(getContaCorrenteDoEmissorFormatado(emissor));
		builder.append("-").append(emissor.getDigitoContaCorrente());
		return builder.toString();
	}

}
