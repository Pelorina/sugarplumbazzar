package Ecommerce.sugar.plum.bazzar.Product;

import Ecommerce.sugar.plum.bazzar.Utils.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public Response create(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/update")
    public Response update(@RequestBody UpdateProduct product) {
        return productService.updateProduct(product);
    }

    @GetMapping("/search")
    public List<ProductData> searchProduct(@RequestParam(name = "category", required = false) String category,
                                           @RequestParam(name = "brandName", required = false) String brandName,
                                           @RequestParam(name = "price", required = false) Double price,
                                           @RequestParam(name = "productName", required = false) String productName
    ) {
        return productService.searchProduct(category, brandName, price, productName);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable(name = "id", required = true) Long id) {
        return productService.deleteProduct(id);
    }


}




