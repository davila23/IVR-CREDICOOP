package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import coop.bancocredicoop.common.domain.numeric.Numeric;
import coop.bancocredicoop.common.exception.ServiceException;
import coop.bancocredicoop.service.proxy.ProxyFactory;
import coop.bancocredicoop.services.interfaces.server.domain.datosbasicos.DatosBasicosResponse;
import coop.bancocredicoop.services.interfaces.server.domain.listacandidatos.ListaCandidatosResponse;
import coop.bancocredicoop.services.interfaces.server.impl.DatosBasicosService;
import coop.bancocredicoop.services.interfaces.server.impl.ListaCandidatosService;

public class TestListaCandidatos {

	@Test
	public void testFindListaCandidatoByNroDocumento() throws ServiceException {
		ListaCandidatosService impl = (ListaCandidatosService) ProxyFactory
				.createServiceProxy(ListaCandidatosService.class);
		List<ListaCandidatosResponse> lista = impl
				.findListaCandidatoByNroDocumento("F", "3772085");
		assertNotNull(lista);
		assertEquals(1, lista.size());
		assertEquals(new Numeric(1001112999),
				((ListaCandidatosResponse) lista.get(0)).getIdPersona());
		assertEquals("VITTO                              ",
				((ListaCandidatosResponse) lista.get(0)).getApellido());
	}

	@Test
	public void testFindDatosBasicosById() throws ServiceException {
		DatosBasicosService impl = (DatosBasicosService) ProxyFactory
				.createServiceProxy(DatosBasicosService.class);
		DatosBasicosResponse response = impl.findDatosBasicosById(new Numeric(
				"1000036665"), "N");
		assertNotNull(response);

	}
}
