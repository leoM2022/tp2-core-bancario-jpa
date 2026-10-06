package com.example.demo.service.impl;

import com.example.demo.service.NotificacionEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Implementación del servicio de notificación por correo electrónico.
 * <p>
 * Gestiona el armado y despacho de correos en formato HTML multipart con soporte UTF-8
 * hacia servidores SMTP para el flujo de activación del cliente.
 * </p>
 *
 * @author Dyevara23 & leoM2022
 * @version 1.2.0
 * @see NotificacionEmailService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionEmailServiceImpl implements NotificacionEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:no-reply@banco.com}")
    private String remitente;

    private static final String URL_ACTIVACION_BASE = "http://localhost:8080/api/v1/clientes/activar?token=";

    @Override
    public void enviarCorreoActivacion(String destinatario, String nombreCliente, String tokenActivacion) {
        String urlActivacion = URL_ACTIVACION_BASE + tokenActivacion;
        String cuerpoHtml = construirPlantillaHtml(nombreCliente, urlActivacion);

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mensaje,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject("¡Bienvenido al Banco! Confirma la activación de tu cuenta bancaria");
            helper.setText(cuerpoHtml, true); // true habilita el renderizado HTML

            mailSender.send(mensaje);
            log.info("[SMTP] Correo de activación despachado con éxito a: {}", destinatario);

        } catch (MessagingException e) {
            log.error("[SMTP-ERROR] Error al despachar el correo de activación hacia {}: {}", destinatario, e.getMessage(), e);
        }
    }

    private String construirPlantillaHtml(String nombre, String link) {
        return """
            <!DOCTYPE html>
            <html lang="es" xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <title>Activación de Cuenta</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #0f172a; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; -webkit-font-smoothing: antialiased; -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%;">
                
                <!-- Preheader oculto para preview en la bandeja de entrada -->
                <div style="display: none; font-size: 1px; color: #0f172a; line-height: 1px; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;">
                    Confirma tu cuenta bancaria para comenzar a operar en la plataforma digital.
                </div>

                <!-- Contenedor externo -->
                <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #0f172a; padding: 40px 10px;">
                    <tr>
                        <td align="center">
                            
                            <!-- Tarjeta principal -->
                            <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 580px; background-color: #ffffff; border-radius: 12px; overflow: hidden; border: 1px solid #1e293b;">
                                
                                <!-- Encabezado institucional -->
                                <tr>
                                    <td style="background-color: #1e293b; padding: 28px 36px; text-align: left; border-bottom: 3px solid #2563eb;">
                                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%">
                                            <tr>
                                                <td>
                                                    <span style="font-size: 18px; font-weight: 800; letter-spacing: 0.5px; color: #ffffff; text-transform: uppercase;">Core Bancario</span>
                                                    <span style="display: block; font-size: 12px; color: #94a3b8; margin-top: 2px;">Banca Digital & Servicios Financieros</span>
                                                </td>
                                                <td align="right">
                                                    <span style="display: inline-block; background-color: rgba(37, 99, 235, 0.2); color: #60a5fa; font-size: 11px; font-weight: 700; padding: 4px 10px; border-radius: 999px; border: 1px solid rgba(96, 165, 250, 0.3);">Seguridad SSL</span>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>

                                <!-- Cuerpo del mensaje -->
                                <tr>
                                    <td style="padding: 36px 36px 20px 36px;">
                                        <h1 style="margin: 0 0 16px 0; font-size: 20px; font-weight: 700; color: #0f172a; line-height: 1.3;">
                                            Activación de credenciales de acceso
                                        </h1>
                                        
                                        <p style="margin: 0 0 14px 0; font-size: 15px; color: #334155; line-height: 1.6;">
                                            Estimado/a <strong style="color: #0f172a;">%s</strong>,
                                        </p>
                                        
                                        <p style="margin: 0 0 24px 0; font-size: 14px; color: #475569; line-height: 1.6;">
                                            Hemos recibido el alta de tu usuario en el sistema. Para garantizar la seguridad del core bancario y habilitar tus operaciones, requerimos que verifiques tu casilla de correo.
                                        </p>

                                        <!-- Botón CTA -->
                                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%" style="margin: 28px 0;">
                                            <tr>
                                                <td align="center">
                                                    <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                                        <tr>
                                                            <td align="center" style="border-radius: 8px; background-color: #2563eb;">
                                                                <a href="%s" target="_blank" style="font-size: 14px; font-weight: 600; color: #ffffff; text-decoration: none; padding: 14px 28px; border-radius: 8px; display: inline-block; letter-spacing: 0.2px;">
                                                                    Confirmar y Activar Cuenta &rarr;
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>

                                        <!-- Caja de advertencia de seguridad -->
                                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #f8fafc; border-radius: 8px; border-left: 4px solid #f59e0b; margin: 24px 0 16px 0;">
                                            <tr>
                                                <td style="padding: 14px 16px;">
                                                    <p style="margin: 0; font-size: 13px; color: #78350f; line-height: 1.5;">
                                                        <strong>Aviso de seguridad:</strong> Este enlace caducará automáticamente en <strong>24 horas</strong> y es válido para una única confirmación.
                                                    </p>
                                                </td>
                                            </tr>
                                        </table>

                                        <!-- Fallback de URL directa -->
                                        <p style="margin: 20px 0 0 0; font-size: 12px; color: #64748b; line-height: 1.5;">
                                            Si el botón no responde, copia y pega este enlace en tu navegador:<br>
                                            <a href="%s" style="color: #2563eb; word-break: break-all; text-decoration: underline;">%s</a>
                                        </p>
                                    </td>
                                </tr>

                                <!-- Pie de página / Seguridad -->
                                <tr>
                                    <td style="background-color: #f8fafc; padding: 24px 36px; border-top: 1px solid #e2e8f0; text-align: left;">
                                        <p style="margin: 0 0 8px 0; font-size: 11px; color: #94a3b8; line-height: 1.5;">
                                            Este es un correo automático generado por el Core Transaccional. Si no solicitaste esta cuenta, puedes desestimar este mensaje; ninguna operación se activará sin tu confirmación.
                                        </p>
                                        <p style="margin: 0; font-size: 11px; color: #94a3b8;">
                                            &copy; Sistema de Gestión Bancaria &bull; Todos los derechos reservados.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(nombre, link, link, link);
    }
}