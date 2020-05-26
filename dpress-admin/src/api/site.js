import service from '@/utils/service'

const baseUrl = '/api/admin/site'

const menuApi = {}

menuApi.listAll = () => {
  return service({
    url: `${baseUrl}/list.json`,
    method: 'get'
  })
}

menuApi.listTree = () => {
  return service({
    url: `${baseUrl}/tree_view.json`,
    method: 'get'
  })
}

menuApi.create = menu => {
  return service({
    url: `${baseUrl}/create.json`,
    data: menu,
    method: 'post'
  })
}

menuApi.delete = menu => {
  return service({
    url: `${baseUrl}/delete.json`,
    data: menu,
    method: 'delete'
  })
}

menuApi.get = menuId => {
  return service({
    url: `${baseUrl}/${menuId}`,
    method: 'get'
  })
}

menuApi.update = (menuId, menu) => {
  return service({
    url: `${baseUrl}/update.json`,
    data: menu,
    method: 'put'
  })
}
menuApi.changesessionsite = data => {
  return service({
    url: `${baseUrl}/changesessionsite.json`,
    data: data,
    method: 'post'
  })
}
export default menuApi
