const jsonHeaders = {
  'Content-Type': 'application/json'
}

async function handleResponse(response) {
  if (!response.ok) {
    let errorMessage = 'Unexpected error'

    try {
      const errorBody = await response.json()
      errorMessage =
        errorBody.message ||
        errorBody.error ||
        JSON.stringify(errorBody)
    } catch {
      errorMessage = await response.text()
    }

    throw new Error(errorMessage)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export async function getProducts() {
  const response = await fetch('/product/api/product')
  return handleResponse(response)
}

export async function createProduct(product) {
  const response = await fetch('/product/api/product', {
    method: 'POST',
    headers: jsonHeaders,
    body: JSON.stringify(product)
  })

  return handleResponse(response)
}

export async function createCart() {
  const response = await fetch('/cart/api/cart', {
    method: 'POST'
  })

  return handleResponse(response)
}

export async function addItemToCart(cartId, item) {
  const response = await fetch(`/cart/api/cart/${cartId}/items`, {
    method: 'POST',
    headers: jsonHeaders,
    body: JSON.stringify(item)
  })

  return handleResponse(response)
}

export async function createSale(sale) {
  const response = await fetch('/sales/api/sales', {
    method: 'POST',
    headers: jsonHeaders,
    body: JSON.stringify(sale)
  })

  return handleResponse(response)
}