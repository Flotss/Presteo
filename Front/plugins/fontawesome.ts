import { library, config } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faGithub, faLinkedin, faTwitter, faFacebook, faGoogle } from '@fortawesome/free-brands-svg-icons'
import { faCalendarCheck, faCheckCircle, faCircleNotch, faLocationDot, faStar, faTimesCircle } from '@fortawesome/free-solid-svg-icons'

library.add(faCalendarCheck, faStar, faLocationDot)
library.add(faGithub, faLinkedin, faTwitter, faFacebook, faGoogle)
library.add(faCheckCircle, faTimesCircle, faCircleNotch)


export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.component('font-awesome-icon', FontAwesomeIcon)
})