import java.util.*;

interface cartItem{
    void displayInfo();
    double getPrice();
}
class Product{
    private String name;
    private double price;

    public Product(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }
    public void displayProductInfo(){
        System.out.println("Product Name: "+name+" Price: "+price);
    }
}

class ProductBundle{
    private String bundleName;
    private List<Product> products;

    public ProductBundle(String bundleName){
        this.bundleName = bundleName;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void removeProduct(Product product){
        products.remove(product);
    }

    public double getTotalPrice(){
        double totalPrice = 0;
        for(Product product : products){
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    public void displayBundleInfo(){
        System.out.println("Bundle Name: "+bundleName);
        System.out.println("Products in the bundle:");
        for(Product product : products){
            product.displayProductInfo();
        }
        System.out.println("Total Price: "+getTotalPrice());
    }
}

void main(){
        //Creating composite pattern
    System.out.println("Composite Pattern");
   Product book= new Product("Book", 10.0);
   Product pen= new Product("Pen", 2.0);
   Product iphone = new Product("Iphone", 1000.0);

   // Bundle iphone combo
   ProductBundle iphoneCombo = new ProductBundle("Iphone Combo");
    iphoneCombo.addProduct(iphone);
    iphoneCombo.addProduct(pen);
    iphoneCombo.addProduct(book);


    // Displaying bundle informatio
    List<Object> cart = new ArrayList<>(); //this is ₹ of the issue composite pattern solves
    cart.add(book);
    cart.add(pen);
    cart.add(iphoneCombo);

    System.out.println("Cart without using composite pattern");
    for(Object item : cart){
        if(item instanceof Product){
            Product product = (Product) item;
            product.displayProductInfo();
        } else if(item instanceof ProductBundle){
            ProductBundle bundle = (ProductBundle) item;
            bundle.displayBundleInfo();         
        }
    }
}

