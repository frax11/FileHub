import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS, withXsrfConfiguration } from '@angular/common/http';

import { routes } from './app.routes';
import { credentialsInterceptor } from './interceptors/credentials-interceptor';

export const appConfig: ApplicationConfig = {
    providers: [
        provideRouter(routes),
        provideHttpClient(
            withInterceptorsFromDi(),      // permette di usare interceptor classici
            withXsrfConfiguration({
                cookieName: 'XSRF-TOKEN',
                headerName: 'X-XSRF-TOKEN'
            })
        ),
        {
            provide: HTTP_INTERCEPTORS,
            useClass: credentialsInterceptor,
            multi: true
        }
    ]
};