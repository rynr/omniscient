package org.rjung.service.tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TagUsage> getIndex() {
        return tagService.getTagUsage();
    }
}
