package io.flashreport.example;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;

import static java.util.Collections.singletonList;

/**
 * This application illustrates how our API can be accessed from a Spring application.
 */
public class SampleApplication {

    public static final String API_TOKEN = "PASTE YOU API TOKEN HERE";
    public static final String BASE_URL = "https://gateway.flashreport.io/api/v1/report";

    public static void main(String[] args) throws Exception {
        SampleApplication sampleApplication = new SampleApplication();
        sampleApplication.simpleCreationAndDownload();
    }

    public void simpleCreationAndDownload() throws Exception {

        String reportTitle = "Customer List";
        String createUrl = BASE_URL + "/new?title=" + reportTitle;

        // Setup HTTP header with BASIC security
        HttpHeaders httpHeaders = new HttpHeaders();
        String basicHeader = "Basic " + Base64.getEncoder().encodeToString((API_TOKEN + ":").getBytes());
        httpHeaders.add("Authorization", basicHeader);

        // Post the generation of a new report
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entityWithBody = new HttpEntity<>(readTestMessage("customers-small.json"), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(createUrl, HttpMethod.POST, entityWithBody, String.class);
        System.out.println(String.format("\nReport creation request to <%s>", createUrl));
        System.out.println(String.format("Response status code : %s", response.getStatusCode()));
        System.out.println(String.format("Response body : %s", response.getBody()));

        // Query the report URL
        String reportUuid = response.getBody();
        String reportStatusUrl = BASE_URL + "/" + reportUuid;
        HttpEntity entity = new HttpEntity<>(httpHeaders);
        response = restTemplate.exchange(reportStatusUrl, HttpMethod.GET, entity, String.class);
        System.out.println(String.format("\nReport consultation request to <%s>", reportStatusUrl));
        System.out.println(String.format("Response status code : %s", response.getStatusCode()));
        System.out.println(String.format("Response body : %s", response.getBody()));

        // Let flashreport generate the report
        Thread.sleep(3000);

        // Get storage createUrl by invoking /storage endpoint
        String reportStorageUrl = BASE_URL + "/" + reportUuid + "/storage";
        response = restTemplate.exchange(reportStorageUrl, HttpMethod.GET, entity, String.class);
        System.out.println(String.format("\nReport storage url request to <%s>", reportStorageUrl));
        System.out.println(String.format("Response status code : %s", response.getStatusCode()));
        System.out.println(String.format("Response body : %s", response.getBody()));

        // Download the PDF from the storage location
        // We do not need any authentication, this is a presigned URL
        // Warning, it has a limited validity in time of about one hour)
        URI uri = new URI(response.getBody());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.valueOf("application/pdf")));

        ResponseEntity<byte[]> pdf = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<byte[]>(headers), byte[].class);

        // Save PDf locally
        Files.write(Paths.get("./downloaded.pdf"), pdf.getBody());
        System.out.println(String.format("\nPDF saved to downloaded.pdf"));
    }


    private String readTestMessage(String fileName) {
        InputStream is = this.getClass().getResourceAsStream("/" + fileName);
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(is, "UTF-8")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                stringBuilder.append(line).append("\n");
            }
            scanner.close();
        }
        return stringBuilder.toString();
    }

}
