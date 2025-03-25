import { library, config } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faGithub, faLinkedin, faTwitter, faFacebook } from '@fortawesome/free-brands-svg-icons'
import { faCalendarCheck, faLocationDot, faStar } from '@fortawesome/free-solid-svg-icons'

library.add(faCalendarCheck, faStar, faLocationDot)
library.add(faGithub, faLinkedin, faTwitter, faFacebook)


export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component('font-awesome-icon', FontAwesomeIcon)
})