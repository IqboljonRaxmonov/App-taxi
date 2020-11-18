package ai.ecma.server.controller;

import ai.ecma.server.entity.Model;
import ai.ecma.server.payload.ModelDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/model")
public class ModelController {

    @Autowired
    ModelService modelService;

    @GetMapping
    public List<Model> getModelList() {
        return modelService.getModelList();
    }

    @PostMapping
    public Result addModel(@RequestBody ModelDto modelDto) {
        return modelService.addModel(modelDto);
    }

    @GetMapping("/{id}")

    public Model getModelById(@PathVariable Integer id) {
        return modelService.getModelById(id);

    }

    @DeleteMapping("/{id}")
    public Result deleteModelById(Integer id) {
        return modelService.deleteModelById(id);
    }

    @PutMapping("/{id}")
    public Result editModel(@PathVariable Integer id, @RequestBody Model model) {
        return modelService.editModel(id, model);
    }

}

