import "core-js/stable";
import "regenerator-runtime/runtime";
import * as queryString from "query-string";

export default async function calendarSearchApi(year) {
  const response = await fetch(`/api/3/calendars/` + year + `?` + queryString.stringify({
    limit: 'all',
  }))
  const data = await response.json()
  if (!data.success) {
    console.log('failed to get calendar browse api response')
    throw new Error(data.message)
  }
  return data
}