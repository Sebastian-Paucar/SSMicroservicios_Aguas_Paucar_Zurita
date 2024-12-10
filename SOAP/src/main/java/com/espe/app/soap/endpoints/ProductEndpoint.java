package com.espe.app.soap.endpoints;

import com.espe.app.soap.models.Producto;
import com.espe.app.soap.repositories.ProductoRepository;
import com.espe.soap.ws.GetProductoRequest;
import com.espe.soap.ws.GetProductoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ProductEndpoint {

    private static final String NAMESPACE_URI = "http://www.example.com/productos";

    private final ProductoRepository productRepository;

    @Autowired
    public ProductEndpoint(ProductoRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetProductRequest")
    @ResponsePayload
    public GetProductoResponse getProduct(@RequestPayload GetProductoRequest request) {
        GetProductoResponse response = new GetProductoResponse();
        Producto product = productRepository.findById(request.getIdProducto()).orElse(null);

        if (product != null) {
            // Convertir la entidad Product a la respuesta SOAP
            com.espe.soap.ws.Producto soapProduct = new com.espe.soap.ws.Producto();
            soapProduct.setIdProducto(product.getIdProducto());
            soapProduct.setNombre(product.getNombre());
            soapProduct.setDescripcion(product.getDescripcion());
            soapProduct.setPrecio(product.getPrecio());
            response.setProducto(soapProduct);
        }

        return response;
    }
}
