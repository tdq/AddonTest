package com.example.addontest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.annotation.WebServlet;

import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("addontest")
@Widgetset("com.example.addontest.widgetset.AddontestWidgetset")
public class AddontestUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = AddontestUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		final SimpleFileDownloader downloader = new SimpleFileDownloader();
		addExtension(downloader);

		final StreamResource resource = getMemoryStream();
		downloader.setFileDownloadResource(resource);
		
		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				downloader.download();
			}
		});
		layout.addComponent(button);

		
	}

	private StreamResource getMemoryStream() {
		return new StreamResource(() -> {
			return new ByteArrayInputStream("This is test clicked on button".getBytes());
		}, "testButton.txt");
	}
	
	private StreamResource getFileResource(File inputFile) {
		StreamResource.StreamSource source = new StreamResource.StreamSource() {

            public InputStream getStream() {
               
                InputStream input=null;
                try
                {
                    input = new  FileInputStream(inputFile);
                } 
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                  return input;

            }
        };
      StreamResource resource = new StreamResource ( source, inputFile.getName());
        return resource;
	}
}