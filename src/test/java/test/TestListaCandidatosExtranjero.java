package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import coop.bancocredicoop.common.domain.banco.ClaveBup;
import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.service.proxy.ProxyFactory;
import coop.bancocredicoop.services.interfaces.server.domain.datosbasicos.DatosBasicosResponse;
import coop.bancocredicoop.services.interfaces.server.domain.listacandidatos.ListaCandidatosResponse;
import coop.bancocredicoop.services.interfaces.server.domain.sx08.SocioPorDocumentoSX08Response;
import coop.bancocredicoop.services.interfaces.server.impl.DatosBasicosService;
import coop.bancocredicoop.services.interfaces.server.impl.ListaCandidatosService;
import coop.bancocredicoop.services.interfaces.server.impl.SocioPorDocumentoSX08Service;

public class TestListaCandidatosExtranjero {

	@Test
	public void testFindListaCandidatoByNroDocumento() throws ServiceException {
		SocioPorDocumentoSX08Service impl = (SocioPorDocumentoSX08Service) ProxyFactory
				.createServiceProxy(SocioPorDocumentoSX08Service.class);
		ClaveBup claveBup = new ClaveBup();
		claveBup.setNumeroDocumento("551424");
		claveBup.setTipoDocumento(new Numeric(9));
		claveBup.setCodProvincia(new Numeric(0));
		List<SocioPorDocumentoSX08Response> lista = impl.findSocio(claveBup);
		assertNotNull(lista);
		assertEquals(1, lista.size());
		// assertEquals(new Numeric(1001112999), ((ListaCandidatosResponse)
		// lista.get(0)).getIdPersona());
		// assertEquals("VITTO                              ",
		// ((ListaCandidatosResponse) lista.get(0)).getApellido());
	}

	@Test
	public void testFindDatosBasicosById() throws ServiceException {
		DatosBasicosService impl = (DatosBasicosService) ProxyFactory
				.createServiceProxy(DatosBasicosService.class);
		DatosBasicosResponse response = impl.findDatosBasicosById(new Numeric(
				"1001112999"), "N");
		assertNotNull(response);

	}
}
