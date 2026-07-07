import { useEffect, useState } from 'react'
import {
  addItemToCart,
  createCart,
  createProduct,
  createSale,
  getProducts
} from './api/applianceApi'
import './App.css'

function App() {
  const [products, setProducts] = useState([])
  const [cartId, setCartId] = useState('')
  const [selectedProductId, setSelectedProductId] = useState('')
  const [quantity, setQuantity] = useState(1)
  const [customerEmail, setCustomerEmail] = useState('customer@example.com')
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const [productForm, setProductForm] = useState({
    code: `FRONT-${Date.now()}`,
    name: 'Smart TV 55',
    brand: 'Samsung',
    price: 850000
  })

  useEffect(() => {
    loadProducts()
  }, [])

  async function executeAction(action, successMessage) {
    setLoading(true)
    setError('')
    setMessage('')

    try {
      const result = await action()
      setMessage(successMessage)
      return result
    } catch (exception) {
      setError(exception.message)
    } finally {
      setLoading(false)
    }
  }

  async function loadProducts() {
    await executeAction(async () => {
      const data = await getProducts()
      setProducts(data)

      if (data.length > 0 && !selectedProductId) {
        setSelectedProductId(data[0].id)
      }

      return data
    }, 'Products loaded successfully')
  }

  async function handleCreateProduct(event) {
    event.preventDefault()

    await executeAction(async () => {
      const product = await createProduct({
        code: productForm.code,
        name: productForm.name,
        brand: productForm.brand,
        price: Number(productForm.price)
      })

      await loadProducts()
      setSelectedProductId(product.id)

      setProductForm({
        ...productForm,
        code: `FRONT-${Date.now()}`
      })

      return product
    }, 'Product created successfully')
  }

  async function handleCreateCart() {
    const cart = await executeAction(
      () => createCart(),
      'Cart created successfully'
    )

    if (cart && cart.id) {
      setCartId(cart.id)
    }
  }

  async function handleAddItem(event) {
    event.preventDefault()

    if (!cartId) {
      setError('Create a cart first')
      return
    }

    if (!selectedProductId) {
      setError('Select a product first')
      return
    }

    await executeAction(
      () =>
        addItemToCart(cartId, {
          productId: Number(selectedProductId),
          quantity: Number(quantity)
        }),
      'Product added to cart successfully'
    )
  }

  async function handleCreateSale(event) {
    event.preventDefault()

    if (!cartId) {
      setError('Create a cart first')
      return
    }

    await executeAction(
      () =>
        createSale({
          cartId: Number(cartId),
          customerEmail
        }),
      'Sale created successfully. Check Mailpit inbox.'
    )
  }

  function updateProductForm(field, value) {
    setProductForm({
      ...productForm,
      [field]: value
    })
  }

  return (
    <main className="page">
      <section className="hero">
        <div>
          <p className="eyebrow">Appliance Store Platform</p>
          <h1>Microservices Demo Frontend</h1>
          <p>
            Minimal React client to test Product, Cart and Sales through the API Gateway.
          </p>
        </div>

        <div className="status-card">
          <span>Gateway</span>
          <strong>http://localhost:8088</strong>
        </div>
      </section>

      {loading && <div className="alert info">Processing...</div>}
      {message && <div className="alert success">{message}</div>}
      {error && <div className="alert error">{error}</div>}

      <section className="grid">
        <article className="card">
          <h2>1. Create Product</h2>

          <form onSubmit={handleCreateProduct} className="form">
            <label>
              Code
              <input
                value={productForm.code}
                onChange={(event) => updateProductForm('code', event.target.value)}
              />
            </label>

            <label>
              Name
              <input
                value={productForm.name}
                onChange={(event) => updateProductForm('name', event.target.value)}
              />
            </label>

            <label>
              Brand
              <input
                value={productForm.brand}
                onChange={(event) => updateProductForm('brand', event.target.value)}
              />
            </label>

            <label>
              Price
              <input
                type="number"
                value={productForm.price}
                onChange={(event) =>
                  updateProductForm('price', event.target.value)
                }
              />
            </label>

            <button type="submit">Create Product</button>
          </form>
        </article>

        <article className="card">
          <h2>2. Products</h2>

          <button type="button" className="secondary" onClick={loadProducts}>
            Refresh Products
          </button>

          <div className="products">
            {products.length === 0 && <p>No products loaded.</p>}

            {products.map((product) => (
              <div className="product" key={product.id}>
                <div>
                  <strong>{product.name}</strong>
                  <span>{product.code}</span>
                  <span>{product.brand}</span>
                </div>

                <div>
                  <span>${product.price}</span>
                </div>
              </div>
            ))}
          </div>
        </article>

        <article className="card">
          <h2>3. Create Cart</h2>

          <p>
            Current cart id:
            <strong className="highlight"> {cartId || 'No cart created yet'}</strong>
          </p>

          <button type="button" onClick={handleCreateCart}>
            Create Cart
          </button>
        </article>

        <article className="card">
          <h2>4. Add Item To Cart</h2>

          <form onSubmit={handleAddItem} className="form">
            <label>
              Product
              <select
                value={selectedProductId}
                onChange={(event) => setSelectedProductId(event.target.value)}
              >
                {products.map((product) => (
                  <option key={product.id} value={product.id}>
                    {product.name} - {product.code}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Quantity
              <input
                type="number"
                min="1"
                value={quantity}
                onChange={(event) => setQuantity(event.target.value)}
              />
            </label>

            <button type="submit">Add Item</button>
          </form>
        </article>

        <article className="card wide">
          <h2>5. Create Sale</h2>

          <form onSubmit={handleCreateSale} className="form">
            <label>
              Customer Email
              <input
                type="email"
                value={customerEmail}
                onChange={(event) => setCustomerEmail(event.target.value)}
              />
            </label>

            <button type="submit">Create Sale</button>
          </form>

          <p className="hint">
            After creating the sale, check Mailpit at http://localhost:8025.
          </p>
        </article>
      </section>
    </main>
  )
}

export default App